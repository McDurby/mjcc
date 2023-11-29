package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

import java.util.List;
import java.util.Optional;

public interface CompensationService {

    Optional<Compensation> create(String employeeId, Compensation compensation);

    Optional<Compensation> read(String employeeId);

    List<Compensation> readAll();
}
