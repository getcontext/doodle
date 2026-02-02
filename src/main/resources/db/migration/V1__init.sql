-- Initial schema for mini Doodle scheduling service
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE IF NOT EXISTS users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(255) UNIQUE NOT NULL,
    display_name varchar(255),
    time_zone varchar(64),
    created_at timestamptz DEFAULT now()
);

CREATE TABLE IF NOT EXISTS calendars (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255) NOT NULL,
    owner_id uuid REFERENCES users(id) ON DELETE CASCADE,
    default_time_zone varchar(64),
    created_at timestamptz DEFAULT now()
);

CREATE TABLE IF NOT EXISTS slots (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    calendar_id uuid REFERENCES calendars(id) ON DELETE CASCADE,
    start_time timestamptz NOT NULL,
    end_time timestamptz NOT NULL,
    capacity int DEFAULT 1,
    reserved_count int DEFAULT 0,
    status varchar(32) DEFAULT 'AVAILABLE',
    created_at timestamptz DEFAULT now(),
    updated_at timestamptz DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_slots_calendar_start_end ON slots (calendar_id, start_time, end_time);
CREATE INDEX IF NOT EXISTS idx_slots_timerange ON slots USING GIST (tstzrange(start_time, end_time));

CREATE TABLE IF NOT EXISTS meetings (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    calendar_id uuid REFERENCES calendars(id) ON DELETE CASCADE,
    title varchar(512) NOT NULL,
    organizer_id uuid REFERENCES users(id),
    start_time timestamptz NOT NULL,
    end_time timestamptz NOT NULL,
    status varchar(32) DEFAULT 'SCHEDULED',
    created_at timestamptz DEFAULT now(),
    updated_at timestamptz DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_meetings_calendar_start_end ON meetings (calendar_id, start_time, end_time);
CREATE INDEX IF NOT EXISTS idx_meetings_timerange ON meetings USING GIST (tstzrange(start_time, end_time));

-- Prevent overlapping meetings in the same calendar using exclusion constraint
ALTER TABLE meetings
  ADD COLUMN IF NOT EXISTS time_range tstzrange GENERATED ALWAYS AS (tstzrange(start_time, end_time)) STORED;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'meetings_no_overlap'
    ) THEN
        ALTER TABLE meetings
        ADD CONSTRAINT meetings_no_overlap EXCLUDE USING GIST (calendar_id WITH =, time_range WITH &&);
    END IF;
END$$;

CREATE TABLE IF NOT EXISTS participants (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    meeting_id uuid REFERENCES meetings(id) ON DELETE CASCADE,
    user_id uuid REFERENCES users(id),
    email varchar(255),
    response varchar(32) DEFAULT 'UNKNOWN',
    responded_at timestamptz
);

CREATE INDEX IF NOT EXISTS idx_participants_email ON participants (email);
