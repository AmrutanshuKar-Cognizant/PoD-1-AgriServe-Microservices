package com.cognizant.agriserve.trainingservice.config;

import com.cognizant.agriserve.trainingservice.entity.Participation;
import com.cognizant.agriserve.trainingservice.entity.TrainingProgram;
import com.cognizant.agriserve.trainingservice.entity.Workshop;
import com.cognizant.agriserve.trainingservice.dao.ParticipationRepository;
import com.cognizant.agriserve.trainingservice.dao.TrainingProgramRepository;
import com.cognizant.agriserve.trainingservice.dao.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final TrainingProgramRepository programRepository;
    private final WorkshopRepository workshopRepository;
    private final ParticipationRepository participationRepository;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            if (programRepository.count() == 0) {
                log.info("Seeding test data into Training Service Database...");

                // 1. Create a Training Program
                TrainingProgram program = new TrainingProgram();
                program.setTitle("Modern Irrigation Techniques");
                program.setDescription("Learn about drip irrigation and water management.");
                program.setStartDate(LocalDate.now().plusDays(10));
                program.setEndDate(LocalDate.now().plusDays(15));
                program.setStatus("Upcoming");
                program.setManagerId(1L); // Links to ProgramManager (test user ID 1)
                
                TrainingProgram savedProgram = programRepository.save(program);

                // 2. Create a Workshop
                Workshop workshop = new Workshop();
                workshop.setTrainingProgram(savedProgram);
                workshop.setLocation("Community Hall, Pune");
                workshop.setDate(LocalDateTime.now().plusDays(12));
                workshop.setStatus("Scheduled");
                workshop.setOfficerId(2L); // Links to ExtensionOfficer (test user ID 2)
                
                Workshop savedWorkshop = workshopRepository.save(workshop);

                // 3. Create a Participation
                Participation participation = new Participation();
                participation.setWorkshop(savedWorkshop);
                participation.setFarmerId(3L); // Links to Farmer (test user ID 3)
                participation.setAttendanceStatus("Pending");
                participation.setFeedback("Looking forward to this!");

                participationRepository.save(participation);

                log.info("Test data seeding complete! You can now log in and test endpoints.");
            } else {
                log.info("Database already contains data, skipping seeder.");
            }
        };
    }
}
