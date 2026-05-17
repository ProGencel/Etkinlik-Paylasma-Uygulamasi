package com.ahmetefe.backend.scheduler;

import com.ahmetefe.backend.entity.Event;
import com.ahmetefe.backend.repository.EventRepository;
import com.ahmetefe.backend.utils.EventState;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventStatusScheduler {

    final EventRepository eventRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void updateEventStatus()
    {
        List<Event> expiredEvents =  eventRepository.findExpiredEvents(LocalDate.now(), LocalTime.now());

        if(!expiredEvents.isEmpty())
        {
            for(Event event : expiredEvents)
            {
                event.setState(EventState.STOPPED);
            }

            eventRepository.saveAll(expiredEvents);
        }
    }

}
