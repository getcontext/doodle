//package com.example.doodle.integration;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class ReserveSlotAndScheduleIT extends IntegrationTestBase {
//
//    @Test
//    public void reserveSlot_then_scheduleMeeting() {
//        // create calendar
//        Map<String,Object> calReq = Map.of("name","IT Cal","ownerId","11111111-1111-1111-1111-111111111111");
//        ResponseEntity<Map> calResp = restTemplate.postForEntity("/api/v1/calendars", calReq, Map.class);
//        assertThat(calResp.getStatusCodeValue()).isEqualTo(201);
//        Map<String,Object> cal = calResp.getBody();
//        String calId = (String) cal.get("id");
//
//        // create slot
//        Map<String,Object> slotReq = Map.of("startTime","2026-02-10T10:00:00Z","endTime","2026-02-10T11:00:00Z","capacity",1);
//        ResponseEntity<Map> slotResp = restTemplate.postForEntity("/api/v1/calendars/"+calId+"/slots", slotReq, Map.class);
//        assertThat(slotResp.getStatusCodeValue()).isEqualTo(201);
//        Map<String,Object> slot = slotResp.getBody();
//        String slotId = (String) slot.get("id");
//
//        // schedule meeting using slotId
//        Map<String,Object> meetReq = Map.of("title","IT Meeting","slotId",slotId,"organizerId","11111111-1111-1111-1111-111111111111","participantEmails", new String[]{"a@x.com"});
//        ResponseEntity<Map> meetResp = restTemplate.postForEntity("/api/v1/meetings", meetReq, Map.class);
//        assertThat(meetResp.getStatusCodeValue()).isEqualTo(201);
//        Map<String,Object> meeting = meetResp.getBody();
//        assertThat(meeting.get("title")).isEqualTo("IT Meeting");
//    }
//}
