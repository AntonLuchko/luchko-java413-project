//package com.example.demo.contollers;
//import com.example.demo.dto.TeachersDTO;
//import com.example.demo.repositories.BearRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.javafaker.Faker;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class HomeControllerTest {
//
//
//    @Autowired
//    private BearRepository bearRepository;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private Faker faker;
//
//    @Autowired
//    private MockMvc mockMvc;
//
////    Задание 1: Базовый CRUD для книг
////Создайте тест-класс HomeControllerTest для тестирования REST API управления учителями.
//// Модель Teachers содержит поля: id, name, surname, email, age.
//// Напишите тесты для создания книги
//    @Test
//    @Order(1)
//    public void save_teacher_return_ok() throws Exception {
//        Teachers teachers = new Teachers();
//        teachers.setName(faker.name().firstName());
//        teachers.setSurname(faker.name().lastName());
//        teachers.setEmail(faker.internet().emailAddress());
//        teachers.setAge(faker.number().numberBetween(1,100));
//
//        mockMvc.perform(post("/api/teacher/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(teachers)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email", is(teachers.getEmail())))
//                .andExpect(jsonPath("$.name", is(teachers.getName())))
//                .andExpect(jsonPath("$.surname", is(teachers.getSurname())))
//                .andExpect(jsonPath("$.age", is(teachers.getAge())));
//    }
////получения книги по существующему ID
//    @Test
//    @Order(2)
//    public void find_exist_id_return_ok_return_teacher() throws Exception {
//        Teachers teachers = new Teachers();
//        teachers.setName(faker.name().firstName());
//        teachers.setSurname(faker.name().lastName());
//        teachers.setEmail(faker.internet().emailAddress());
//        teachers.setAge(faker.number().numberBetween(1,100));
//       Teachers result= bearRepository.save(teachers);
//       TeachersDTO teachersDTO=new TeachersDTO(result);
//
//        mockMvc.perform(get("/api/teacher/getById/{id}",result.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(teachersDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.emailDTO", is(teachersDTO.getEmailDTO())))
//                .andExpect(jsonPath("$.nameDTO", is(teachersDTO.getNameDTO())))
//                .andExpect(jsonPath("$.surnameDTO", is(teachersDTO.getSurnameDTO())))
//                .andExpect(jsonPath("$.ageDTO", is(teachersDTO.getAgeDTO())))
//                .andExpect(jsonPath("$.idDTO").value(teachersDTO.getIdDTO()));
//    }
//
////    получения книги по несуществующему ID (должен возвращать 404).
//    @Test
//    @Order(3)
//    public void find_not_exist_id_return_ok_return_not_found() throws Exception {
//        mockMvc.perform(get("/api/teacher/getById/{id}",-1))
//                .andExpect(status().isNotFound());
//    }
//
////    Задание 2: Тестирование валидации данных
////Протестируйте валидацию при создании книги.
//// Напишите тесты для: создания учителя с пустым знаенем name (должен возвращать 400),
//@Test
//@Order(4)
//public void create_bad_name_return_bad_request() throws Exception {
//    Teachers teachers = new Teachers();
//    teachers.setName("    ");
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//
//
//    mockMvc.perform(post("/api/teacher/create",result.getId()))
//            .andExpect(status().isBadRequest());
//}
//// создания книги с где отсутствием в email "@" (должен возвращать 400),
//@Test
//@Order(5)
//public void create_bad_email_return_bad_request() throws Exception {
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail("anton.lucko/mail.ru");
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//
//
//    mockMvc.perform(post("/api/teacher/create",result.getId()))
//            .andExpect(status().isBadRequest());
//}
//// создания книги с отрицательным age (должен возвращать 400),
//@Test
//@Order(6)
//public void create_bad_age_return_bad_request() throws Exception {
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(-15);
//    Teachers result= bearRepository.save(teachers);
//
//    mockMvc.perform(post("/api/teacher/create",result.getId()))
//            .andExpect(status().isBadRequest());
//}
//
//
////Задание 3: Тестирование обновления книг
////Создайте тесты для PUT endpoint обновления книг.
//// Протестируйте: полное обновление существующей книги,
//@Test
//@Order(7)
//public void full_update_teacher_return_teacher_dto() throws Exception {
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//
//    TeachersDTO teachersDTO = new TeachersDTO();
//    teachersDTO.setNameDTO(faker.name().firstName());
//    teachersDTO.setSurnameDTO(faker.name().lastName());
//    teachersDTO.setEmailDTO(faker.internet().emailAddress());
//    teachersDTO.setAgeDTO(faker.number().numberBetween(1,100));
//
//    mockMvc.perform(put("/api/teacher/updateById/{id}",result.getId()) .
//            contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(teachersDTO)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.emailDTO", is(teachersDTO.getEmailDTO())))
//            .andExpect(jsonPath("$.nameDTO", is(teachersDTO.getNameDTO())))
//            .andExpect(jsonPath("$.surnameDTO", is(teachersDTO.getSurnameDTO())))
//            .andExpect(jsonPath("$.ageDTO", is(teachersDTO.getAgeDTO())))
//            .andExpect(jsonPath("$.idDTO").value(teachers.getId()));
//}
//// обновление несуществующей книги (404),
//@Test
//@Order(8)
//public void update_not_exist_id_return_ok_return_not_found() throws Exception {
//    TeachersDTO teachersDTO = new TeachersDTO();
//    teachersDTO.setNameDTO(faker.name().firstName());
//    teachersDTO.setSurnameDTO(faker.name().lastName());
//    teachersDTO.setEmailDTO(faker.internet().emailAddress());
//    teachersDTO.setAgeDTO(faker.number().numberBetween(1,100));
//
//    mockMvc.perform(put("/api/teacher/updateById/{id}",-1) .
//            contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(teachersDTO))).
//            andExpect(status().isNotFound());
//}
//
//// обновление с невалидными данными (400).
//@Test
//@Order(9)
//public void full_update_bad_name_teacher_return_bad_request() throws Exception {
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//
//    TeachersDTO teachersDTO = new TeachersDTO();
//    teachersDTO.setNameDTO("   ");
//    teachersDTO.setSurnameDTO(faker.name().lastName());
//    teachersDTO.setEmailDTO(faker.internet().emailAddress());
//    teachersDTO.setAgeDTO(faker.number().numberBetween(1,100));
//
//    mockMvc.perform(put("/api/teacher/updateById/{id}",result.getId()) .
//                    contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(teachersDTO)))
//            .andExpect(status().isBadRequest());
//}
//
//    @Test
//    @Order(10)
//    public void full_update_bad_age_teacher_return_bad_request() throws Exception {
//        Teachers teachers = new Teachers();
//        teachers.setName(faker.name().firstName());
//        teachers.setSurname(faker.name().lastName());
//        teachers.setEmail(faker.internet().emailAddress());
//        teachers.setAge(faker.number().numberBetween(1,100));
//        Teachers result= bearRepository.save(teachers);
//
//        TeachersDTO teachersDTO = new TeachersDTO();
//        teachersDTO.setNameDTO(faker.name().firstName());
//        teachersDTO.setSurnameDTO(faker.name().lastName());
//        teachersDTO.setEmailDTO(faker.internet().emailAddress());
//        teachersDTO.setAgeDTO(-5);
//
//        mockMvc.perform(put("/api/teacher/updateById/{id}",result.getId()) .
//                        contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(teachersDTO)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @Order(11)
//    public void full_update_bad_email_teacher_return_bad_request() throws Exception {
//        Teachers teachers = new Teachers();
//        teachers.setName(faker.name().firstName());
//        teachers.setSurname(faker.name().lastName());
//        teachers.setEmail(faker.internet().emailAddress());
//        teachers.setAge(faker.number().numberBetween(1,100));
//        Teachers result= bearRepository.save(teachers);
//
//        TeachersDTO teachersDTO = new TeachersDTO();
//        teachersDTO.setNameDTO(faker.name().firstName());
//        teachersDTO.setSurnameDTO(faker.name().lastName());
//        teachersDTO.setEmailDTO("anton./mail.ru");
//        teachersDTO.setAgeDTO(faker.number().numberBetween(1,100));
//
//        mockMvc.perform(put("/api/teacher/updateById/{id}",result.getId()) .
//                        contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(teachersDTO)))
//                .andExpect(status().isBadRequest());
//    }
//
////    Задание 4: Поиск и фильтрация книг
////Напишите тесты для поиска учителей по различным критериям:
//void generationTeachers(){
//        if(bearRepository.count()>150)
//            return;
//    for (int i = 0; i < 150; i++) {
//        Teachers teachers = new Teachers();
//        teachers.setName(faker.name().firstName());
//        teachers.setSurname(faker.name().lastName());
//        teachers.setEmail(faker.internet().emailAddress());
//        teachers.setAge(faker.number().numberBetween(1,100));
//        bearRepository.save(teachers);
//    }
//}
//// поиск по фамилии,
//@Test
//@Order(12)
//public void find_by_surname_return_ok_return_teachersDTO() throws Exception {
//      generationTeachers();
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//    TeachersDTO teachersDTO=new TeachersDTO(result);
//
//    mockMvc.perform(get("/api/teacher/findBySurname?surname="+teachersDTO.getSurnameDTO())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(1))));
//}
//// поиск по email (частичное совпадение),
//@Test
//@Order(13)
//public void find_by_email_return_ok_return_teachersDTO() throws Exception {
//    bearRepository.deleteAll();
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//    TeachersDTO teachersDTO=new TeachersDTO(result);
//
//    mockMvc.perform(get("/api/teacher/findByEmail?email="+teachersDTO.getSurnameDTO().replace("@",""))
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(1))));
//}
//
//// фильтрация по диапазону возрасту,
//@Test
//@Order(14)
//public void find_by_age_between_return_ok_return_teachersDTO() throws Exception {
// generationTeachers();
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//    TeachersDTO teachersDTO=new TeachersDTO(result);
//
//    mockMvc.perform(get("/api/teacher/ageBetween?minAge="+teachersDTO.getAgeDTO()+"&maxAge=100")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(1))));
//}
//// комбинированный поиск по нескольким параметрам.
//@Test
//@Order(15)
//public void find_by_name_and_surname_between_return_ok_return_teachersDTO() throws Exception {
//  generationTeachers();
//    Teachers teachers = new Teachers();
//    teachers.setName(faker.name().firstName());
//    teachers.setSurname(faker.name().lastName());
//    teachers.setEmail(faker.internet().emailAddress());
//    teachers.setAge(faker.number().numberBetween(1,100));
//    Teachers result= bearRepository.save(teachers);
//    TeachersDTO teachersDTO=new TeachersDTO(result);
//
//    mockMvc.perform(get("/api/teacher/findByNameAndSurname?name="+teachersDTO.getNameDTO()+"&surname="+teachersDTO.getSurnameDTO())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(1))));
//}
//
////Задание 5: Пагинация и сортировка
////Протестируйте пагинацию списка книг.
//// Создайте тесты для: получения первой страницы с лимитом 10,
//@Test
//@Order(16)
//public void first_page_10_teachers_return_ok_return_teachers() throws Exception {
//    generationTeachers();
//    mockMvc.perform(get("/api/teacher/firstPage"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(10))));
//}
//// получения конкретной страницы,
//@Test
//@Order(17)
//public void concrete_page_return_ok_return_teachers() throws Exception {
//    generationTeachers();
//    mockMvc.perform(get("/api/teacher/concretePage?page=3"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(10))));
//}
////если нет такой конкретной страницы
//    @Test
//    @Order(18)
//    public void concrete_page_return_noContent() throws Exception {
//        bearRepository.deleteAll();
//        mockMvc.perform(get("/api/teacher/concretePage?page=1"))
//                .andExpect(status().isNoContent());
//
//    }
//// сортировки по возрасту (по возрастанию и убыванию),
//@Test
//@Order(19)
//public void sort_by_age_asc_or_desc_return_ok_return_teachers() throws Exception {
//    generationTeachers();
//    mockMvc.perform(get("/api/teacher/sortByAge?type=asc"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$",hasSize(greaterThanOrEqualTo(150))));
//}
//// получения пустой страницы при превышении лимита.
////    здесь можно указать метод api/teacher/firstPage удалив перед этим все данные.
//@Test
//@Order(20)
//public void null_page_return_noContent() throws Exception {
//    bearRepository.deleteAll();
//    mockMvc.perform(get("/api/teacher/firstPage"))
//            .andExpect(status().isNoContent());
//
//}
//}
