package com.example.adrentar.service;

import com.example.adrentar.dto.ResumenInquilinoDto;

import java.time.LocalDate;
import java.util.Date;

public interface InquilinoResumenService {

    ResumenInquilinoDto obtenerResumen(Long idAlquiler, Long idInquilino);

    LocalDate toLocalDate(Date fecha);
}
