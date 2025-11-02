package com.example.excel_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.examples.Example;
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
                .description("Ошибка валидации или неверный формат файла")
                .content(new Content().addMediaType("application/json",
                        new MediaType().examples(createErrorExamples())))
        );

        return responses;
    }

    private Object createSuccessExample() {
        Map<String, String> example = new HashMap<>();
        example.put("answer", "5");
        return example;
    }

    private Map<String, Example> createErrorExamples() {
        Map<String, Example> examples = new HashMap<>();

        Example fileNotFoundExample = new Example();
        fileNotFoundExample.setSummary("Файл не найден");
        fileNotFoundExample.setValue(createErrorValue("Файл не найден по пути: C:/data/numbers.xlsx"));
        examples.put("fileNotFound", fileNotFoundExample);

        Example invalidFormatExample = new Example();
        invalidFormatExample.setSummary("Неверный формат файла");
        invalidFormatExample.setValue(createErrorValue("Файл должен быть в формате .xlsx"));
        examples.put("invalidFormat", invalidFormatExample);

        Example invalidNExample = new Example();
        invalidNExample.setSummary("Некорректный параметр N");
        invalidNExample.setValue(createErrorValue("N должно быть от 1 до 10"));
        examples.put("invalidN", invalidNExample);

        return examples;
    }

    private Map<String, String> createErrorValue(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}