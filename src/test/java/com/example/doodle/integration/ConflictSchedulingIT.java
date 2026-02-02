//package com.example.doodle.integration;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class ConflictSchedulingIT extends IntegrationTestBase {
//
//    @Test
//    public void scheduling_conflict_returns409() {
//        // create calendar
//        Map<String,Object> calReq = Map.of("name","IT Cal 2","ownerId","11111111-1111-1111-1111-111111111111");
//        ResponseEntity<Map> calResp = restTemplate.postForEntity("/api/v1/calendars", calReq, Map.class);
//        assertThat(calResp.getStatusCodeValue()).isEqualTo(201);
//        Map<String,Object> cal = calResp.getBody();
//        String calId = (String) cal.get("id");
//
//        // schedule first meeting by start/end
//        Map<String,Object> m1 = Map.of("title","M1","calendarId",calId,"organizerId","11111111-1111-1111-1111-111111111111","startTime","2026-03-01T10:00:00Z","endTime","2026-03-01T11:00:00Z");
//        ResponseEntity<Map> r1 = restTemplate.postForEntity("/api/v1/meetings", m1, Map.class);
//        assertThat(r1.getStatusCodeValue()).isEqualTo(201);
//
//        // schedule conflicting meeting
//        Map<String,Object> m2 = Map.of("title","M2","calendarId",calId,"organizerId","11111111-1111-1111-1111-111111111111","startTime","2026-03-01T10:30:00Z","endTime","2026-03-01T11:30:00Z");
//        ResponseEntity<Map> r2 = restTemplate.postForEntity("/api/v1/meetings", m2, Map.class);
//        assertThat(r2.getStatusCodeValue()).isEqualTo(409);
//        Map<String,Object> err = r2.getBody();
//        assertThat(err.get("message")).isEqualTo("Meeting conflicts with existing meeting");
//    }
//}
