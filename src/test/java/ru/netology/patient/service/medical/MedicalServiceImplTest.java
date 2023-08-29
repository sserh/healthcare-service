package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

class MedicalServiceImplTest {

    @Test
    void checkBloodPressure_should_be_message() {
        //моким сервисы
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        //данные для эмуляции инфы по пациенту
        PatientInfo patientInfo = new PatientInfo("1", "TestName", "TestSurname", LocalDate.of(2000, 12, 12), new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80)));
        //будем возвращать инфу выше по запросу
        Mockito.when(patientInfoFileRepository.getById("1")).thenReturn(patientInfo);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkBloodPressure("1", new BloodPressure(200, 100));

        //проверим, что метод send(String s) вызывался
        Mockito.verify(sendAlertService).send(Mockito.anyString());

    }

    @Test
    void checkTemperature_should_be_message() {
        //моким сервисы
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        //данные для эмуляции инфы по пациенту
        PatientInfo patientInfo = new PatientInfo("1", "TestName", "TestSurname", LocalDate.of(2000, 12, 12), new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80)));
        //будем возвращать инфу выше по запросу
        Mockito.when(patientInfoFileRepository.getById("1")).thenReturn(patientInfo);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        //странно, но для вывода сообщения температура должна быть более чем на полтора градуса ниже нормальной, ну ок - проверяем
        medicalService.checkTemperature("1", new BigDecimal("35.0"));

        //проверим, что метод send(String s) вызывался
        Mockito.verify(sendAlertService).send(Mockito.anyString());
    }

    @Test
    void checkInfo_should_not_be_message() {
        //моким сервисы
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        //данные для эмуляции инфы по пациенту
        PatientInfo patientInfo = new PatientInfo("1", "TestName", "TestSurname", LocalDate.of(2000, 12, 12), new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80)));
        //будем возвращать инфу выше по запросу
        Mockito.when(patientInfoFileRepository.getById("1")).thenReturn(patientInfo);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        //проверяем нормальную температуру
        medicalService.checkTemperature("1", new BigDecimal("36.6"));
        //проверяем нормальное давление
        medicalService.checkBloodPressure("1", new BloodPressure(120, 80));

        //проверим, что метод send(String s) ни разу не вызывался
        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.anyString());
    }
}