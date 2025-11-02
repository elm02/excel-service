package com.example.excel_service.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class ExcelService {

    public int findSmallestNumber(String filePath, int n) throws Exception {
        validateInput(filePath, n);

        int[] numbers = readNumbersFromExcel(filePath);

        if (n <= 0 || n > numbers.length) {
            throw new IllegalArgumentException("N должно быть от 1 до " + numbers.length);
        }

        return findSmallest(numbers, n);
    }

    private void validateInput(String filePath, int n) {
        if (!filePath.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Файл должен быть в формате .xlsx");
        }

        if (n <= 0) {
            throw new IllegalArgumentException("N должно быть положительным числом");
        }
    }

    private int[] readNumbersFromExcel(String filePath) throws Exception {
         File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден по пути: " + filePath);
        }

        try (FileInputStream fileSteam = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileSteam)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int[] numbers = new int[rowCount];
            int index = 0;

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    numbers[index++] = (int) cell.getNumericCellValue();
                }
            }

            int[] result = new int[index];
            System.arraycopy(numbers, 0, result, 0, index);
            return result;
        }
    }

    private int findSmallest(int[] numbers, int n) {
        int answerIndex = n - 1;
        int leftBorder = 0;
        int rightBorder = numbers.length - 1;

        while (leftBorder <= rightBorder) {
            int pivotIndex = partition(numbers, leftBorder, rightBorder);

            if (pivotIndex == answerIndex) {
                return numbers[answerIndex];
            } else if (pivotIndex > answerIndex) {
                rightBorder = pivotIndex - 1;
            } else {
                leftBorder = pivotIndex + 1;
            }
        }

        return numbers[answerIndex];
    }

    private int partition(int[] numbers, int left, int right) {
        int pivot = numbers[right];
        int i = left;

        for (int j = left; j < right; j++) {
            if (numbers[j] <= pivot) {
                swap(numbers, i, j);
                i++;
            }
        }

        swap(numbers, i, right);
        return i;
    }

    private void swap(int[] array, int index1, int index2) {
        int tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }
}