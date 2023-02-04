package com.codebridgecommunity.myjavaapp3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.codebridgecommunity.myjavaapp3.IntegrationTest;
import com.codebridgecommunity.myjavaapp3.domain.Students;
import com.codebridgecommunity.myjavaapp3.repository.StudentsRepository;
import com.codebridgecommunity.myjavaapp3.service.dto.StudentsDTO;
import com.codebridgecommunity.myjavaapp3.service.mapper.StudentsMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link StudentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class StudentsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Students students;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Students createEntity() {
        Students students = new Students().name(DEFAULT_NAME).email(DEFAULT_EMAIL).age(DEFAULT_AGE);
        return students;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Students createUpdatedEntity() {
        Students students = new Students().name(UPDATED_NAME).email(UPDATED_EMAIL).age(UPDATED_AGE);
        return students;
    }

    @BeforeEach
    public void initTest() {
        studentsRepository.deleteAll().block();
        students = createEntity();
    }

    @Test
    void createStudents() throws Exception {
        int databaseSizeBeforeCreate = studentsRepository.findAll().collectList().block().size();
        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeCreate + 1);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudents.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudents.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    void createStudentsWithExistingId() throws Exception {
        // Create the Students with an existing ID
        students.setId("existing_id");
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        int databaseSizeBeforeCreate = studentsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().collectList().block().size();
        // set the field null
        students.setName(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentsRepository.findAll().collectList().block().size();
        // set the field null
        students.setEmail(null);

        // Create the Students, which fails.
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllStudents() {
        // Initialize the database
        studentsRepository.save(students).block();

        // Get all the studentsList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(students.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].age")
            .value(hasItem(DEFAULT_AGE));
    }

    @Test
    void getStudents() {
        // Initialize the database
        studentsRepository.save(students).block();

        // Get the students
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, students.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(students.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.age")
            .value(is(DEFAULT_AGE));
    }

    @Test
    void getNonExistingStudents() {
        // Get the students
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingStudents() throws Exception {
        // Initialize the database
        studentsRepository.save(students).block();

        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();

        // Update the students
        Students updatedStudents = studentsRepository.findById(students.getId()).block();
        updatedStudents.name(UPDATED_NAME).email(UPDATED_EMAIL).age(UPDATED_AGE);
        StudentsDTO studentsDTO = studentsMapper.toDto(updatedStudents);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, studentsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudents.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudents.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    void putNonExistingStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();
        students.setId(UUID.randomUUID().toString());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, studentsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();
        students.setId(UUID.randomUUID().toString());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();
        students.setId(UUID.randomUUID().toString());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStudentsWithPatch() throws Exception {
        // Initialize the database
        studentsRepository.save(students).block();

        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();

        // Update the students using partial update
        Students partialUpdatedStudents = new Students();
        partialUpdatedStudents.setId(students.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStudents.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStudents))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudents.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudents.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    void fullUpdateStudentsWithPatch() throws Exception {
        // Initialize the database
        studentsRepository.save(students).block();

        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();

        // Update the students using partial update
        Students partialUpdatedStudents = new Students();
        partialUpdatedStudents.setId(students.getId());

        partialUpdatedStudents.name(UPDATED_NAME).email(UPDATED_EMAIL).age(UPDATED_AGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStudents.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStudents))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
        Students testStudents = studentsList.get(studentsList.size() - 1);
        assertThat(testStudents.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudents.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudents.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    void patchNonExistingStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();
        students.setId(UUID.randomUUID().toString());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, studentsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();
        students.setId(UUID.randomUUID().toString());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStudents() throws Exception {
        int databaseSizeBeforeUpdate = studentsRepository.findAll().collectList().block().size();
        students.setId(UUID.randomUUID().toString());

        // Create the Students
        StudentsDTO studentsDTO = studentsMapper.toDto(students);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(studentsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Students in the database
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStudents() {
        // Initialize the database
        studentsRepository.save(students).block();

        int databaseSizeBeforeDelete = studentsRepository.findAll().collectList().block().size();

        // Delete the students
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, students.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Students> studentsList = studentsRepository.findAll().collectList().block();
        assertThat(studentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
