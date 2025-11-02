package com.example.excel_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI excelServiceOpenAPI() {
        return new OpenAPI()
                .paths(new Paths()
                        .addPathItem("/api/excel/find-smallest", new PathItem()
                                .get(new Operation()
                                        .summary("Поиск N-ного минимального числа из Excel файла")
                                        .addParametersItem(new Parameter()
                                                .name("filePath")
                                                .in("query")
                                                .required(true)
                                                .description("Абсолютный путь к локальному файлу в формате xlsx")
                                                .schema(new StringSchema().example("C:/data/numbers.xlsx"))
                                        )
                                        .addParametersItem(new Parameter()
                                                .name("n")
                                                .in("query")
                                                .required(true)
                                                .description("Порядковый номер минимального числа")
                                                .schema(new IntegerSchema().example(3))
                                        )
                                        .responses(createApiResponses())
                                )
                        )
                );
    }

    private ApiResponses createApiResponses() {
        ApiResponses responses = new ApiResponses();

        responses.addApiResponse("200", new ApiResponse()
                .description("Успешный поиск N-ного минимального числа")
                .content(new Content().addMediaType("application/json",
                        new MediaType().example(createSuccessExample())))
        );

        responses.addApiResponse("400", new ApiResponse()
                .description("Файл не найден")
                .content(new Content().addMediaType("application/json",
                        new MediaType().example(createFileNotFoundExample())))
        );

        responses.addApiResponse("400", new ApiResponse()
                .description("Неверный формат файла")
                .content(new Content().addMediaType("application/json",
                        new MediaType().example(createInvalidFormatExample())))
        );

        responses.addApiResponse("400", new ApiResponse()
                .description("Некорректный параметр N")
                .content(new Content().addMediaType("application/json",
                        new MediaType().example(createInvalidNExample())))
        );

        return responses;
    }

    private Object createSuccessExample() {
        Map<String, String> example = new HashMap<>();
        example.put("answer", "5");
        return example;
    }

    private Object createFileNotFoundExample() {
        Map<String, String> example = new HashMap<>();
        example.put("error", "Файл не найден по пути: C:/data/numbers.xlsx");
        return example;
    }

    private Object createInvalidFormatExample() {
        Map<String, String> example = new HashMap<>();
        example.put("error", "Файл должен быть в формате .xlsx");
        return example;
    }

    private Object createInvalidNExample() {
        Map<String, String> example = new HashMap<>();
        example.put("error", "N должно быть от 1 до 10");
        return example;
    }
}