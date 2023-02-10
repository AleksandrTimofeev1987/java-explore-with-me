package ru.practicum.event.service;

import ru.practicum.event.dto.RateEventCreate;
import ru.practicum.event.dto.RateEventResponse;

import java.util.List;

public interface RateEventPrivateService {

    /**
     * Method gets events rates created by user.
     *
     * @param userId ID of user requesting rates.
     *
     * @return List of events rates.
     */
    List<RateEventResponse> getRatesByCreator(Long userId);

    /**
     * Method rates event.
     *
     * @param rateEventDto Rate to be added.
     *
     * @return Added rate with assigned ID.
     */
    RateEventResponse rateEvent(RateEventCreate rateEventDto);

    /**
     * Method deletes rate for event.
     *
     * @param userId User deleting it's rate.
     * @param rateId ID of rate to be deleted.
     */
    void deleteRateEvent(Long userId, Long rateId);

    /**
     * Method update rate for event.
     *
     * @param userId ID of user requesting update.
     * @param rateId ID of rate to be updated.
     * @param rate New rate value.
     *
     * @return Updated rate.
     */
    RateEventResponse updateRate(Long userId, Long rateId, Integer rate);
}
