package com.codebridgecommunity.myjavaapp3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.codebridgecommunity.myjavaapp3.IntegrationTest;
import com.codebridgecommunity.myjavaapp3.domain.Classes;
import com.codebridgecommunity.myjavaapp3.repository.ClassesRepository;
import com.codebridgecommunity.myjavaapp3.service.dto.ClassesDTO;
import com.codebridgecommunity.myjavaapp3.service.mapper.ClassesMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ClassesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ClassesResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TEACHER = "AAAAAAAAAA";
    private static final String UPDATED_TEACHER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Classes classes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classes createEntity() {
        Classes classes = new Classes().className(DEFAULT_CLASS_NAME).time(DEFAULT_TIME).teacher(DEFAULT_TEACHER);
        return classes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classes createUpdatedEntity() {
        Classes classes = new Classes().className(UPDATED_CLASS_NAME).time(UPDATED_TIME).teacher(UPDATED_TEACHER);
        return classes;
    }

    @BeforeEach
    public void initTest() {
        classesRepository.deleteAll().block();
        classes = createEntity();
    }

    @Test
    void createClasses() throws Exception {
        int databaseSizeBeforeCreate = classesRepository.findAll().collectList().block().size();
        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeCreate + 1);
        Classes testClasses = classesList.get(classesList.size() - 1);
        assertThat(testClasses.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testClasses.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testClasses.getTeacher()).isEqualTo(DEFAULT_TEACHER);
    }

    @Test
    void createClassesWithExistingId() throws Exception {
        // Create the Classes with an existing ID
        classes.setId("existing_id");
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        int databaseSizeBeforeCreate = classesRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllClasses() {
        // Initialize the database
        classesRepository.save(classes).block();

        // Get all the classesList
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
            .value(hasItem(classes.getId()))
            .jsonPath("$.[*].className")
            .value(hasItem(DEFAULT_CLASS_NAME))
            .jsonPath("$.[*].time")
            .value(hasItem(DEFAULT_TIME.toString()))
            .jsonPath("$.[*].teacher")
            .value(hasItem(DEFAULT_TEACHER));
    }

    @Test
    void getClasses() {
        // Initialize the database
        classesRepository.save(classes).block();

        // Get the classes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, classes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(classes.getId()))
            .jsonPath("$.className")
            .value(is(DEFAULT_CLASS_NAME))
            .jsonPath("$.time")
            .value(is(DEFAULT_TIME.toString()))
            .jsonPath("$.teacher")
            .value(is(DEFAULT_TEACHER));
    }

    @Test
    void getNonExistingClasses() {
        // Get the classes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingClasses() throws Exception {
        // Initialize the database
        classesRepository.save(classes).block();

        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();

        // Update the classes
        Classes updatedClasses = classesRepository.findById(classes.getId()).block();
        updatedClasses.className(UPDATED_CLASS_NAME).time(UPDATED_TIME).teacher(UPDATED_TEACHER);
        ClassesDTO classesDTO = classesMapper.toDto(updatedClasses);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
        Classes testClasses = classesList.get(classesList.size() - 1);
        assertThat(testClasses.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testClasses.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testClasses.getTeacher()).isEqualTo(UPDATED_TEACHER);
    }

    @Test
    void putNonExistingClasses() throws Exception {
        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();
        classes.setId(UUID.randomUUID().toString());

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClasses() throws Exception {
        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();
        classes.setId(UUID.randomUUID().toString());

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClasses() throws Exception {
        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();
        classes.setId(UUID.randomUUID().toString());

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClassesWithPatch() throws Exception {
        // Initialize the database
        classesRepository.save(classes).block();

        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();

        // Update the classes using partial update
        Classes partialUpdatedClasses = new Classes();
        partialUpdatedClasses.setId(classes.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClasses.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClasses))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
        Classes testClasses = classesList.get(classesList.size() - 1);
        assertThat(testClasses.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testClasses.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testClasses.getTeacher()).isEqualTo(DEFAULT_TEACHER);
    }

    @Test
    void fullUpdateClassesWithPatch() throws Exception {
        // Initialize the database
        classesRepository.save(classes).block();

        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();

        // Update the classes using partial update
        Classes partialUpdatedClasses = new Classes();
        partialUpdatedClasses.setId(classes.getId());

        partialUpdatedClasses.className(UPDATED_CLASS_NAME).time(UPDATED_TIME).teacher(UPDATED_TEACHER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClasses.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClasses))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
        Classes testClasses = classesList.get(classesList.size() - 1);
        assertThat(testClasses.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testClasses.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testClasses.getTeacher()).isEqualTo(UPDATED_TEACHER);
    }

    @Test
    void patchNonExistingClasses() throws Exception {
        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();
        classes.setId(UUID.randomUUID().toString());

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, classesDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClasses() throws Exception {
        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();
        classes.setId(UUID.randomUUID().toString());

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClasses() throws Exception {
        int databaseSizeBeforeUpdate = classesRepository.findAll().collectList().block().size();
        classes.setId(UUID.randomUUID().toString());

        // Create the Classes
        ClassesDTO classesDTO = classesMapper.toDto(classes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Classes in the database
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClasses() {
        // Initialize the database
        classesRepository.save(classes).block();

        int databaseSizeBeforeDelete = classesRepository.findAll().collectList().block().size();

        // Delete the classes
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, classes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Classes> classesList = classesRepository.findAll().collectList().block();
        assertThat(classesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
