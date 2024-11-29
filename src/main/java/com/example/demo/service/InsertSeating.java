package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//@Transactional
//@Service
public class InsertSeating {
	 public static void main(String[] args) {
	        // 設定分區及其座位參數
	        Map<String, int[]> sections = new LinkedHashMap<>();
	        sections.put("seats", new int[]{10, 8}); // 單區域，3 行，每行 8 列
//	        sections.put("front", new int[]{2, 6}); // 前區，2 行，每行 6 列
//	        sections.put("back", new int[]{3, 6}); // 後區，3 行，每行 6 列
//	        sections.put("left", new int[]{3, 4}); // 左區域（僅示例）
//	        sections.put("right", new int[]{3, 4}); // 右區域（僅示例）

	        // 設定走道位置（從 1 開始）
	        List<Integer> walkwayPositions = List.of(3, 6);

	        // 生成座位表
	        Map<String, List<Map<String, Object>>> seatingLayout = generateSeatingLayout(sections, walkwayPositions);

	        // 打印結果
	        seatingLayout.forEach((section, rows) -> {
	            System.out.println("Section: " + section);
	            rows.forEach(row -> System.out.println("  Row " + row.get("row") + ": " + row.get("seats")));
	        });
	    }

	    /**
	     * 根據分區和參數生成座位表
	     * @param sections 分區設置，每個分區對應行數和列數
	     * @param walkwayPositions 走道的位置
	     * @return 分區座位表
	     */
	    public static Map<String, List<Map<String, Object>>> generateSeatingLayout(
	            Map<String, int[]> sections, List<Integer> walkwayPositions) {

	        Map<String, List<Map<String, Object>>> layout = new LinkedHashMap<>();
	        char rowLabel = 'A'; // 初始行標籤

	        for (Map.Entry<String, int[]> section : sections.entrySet()) {
	            String sectionName = section.getKey();
	            int[] dimensions = section.getValue();
	            int rows = dimensions[0];
	            int columns = dimensions[1];

	            if (rows <= 0 || columns <= 0) {
	                // 如果分區的行數或列數無效，跳過
	                System.out.println("Skipping invalid section: " + sectionName);
	                continue;
	            }

	            List<Map<String, Object>> sectionRows = new ArrayList<>();
	            for (int i = 0; i < rows; i++) {
	                Map<String, Object> row = new HashMap<>();
	                row.put("row", String.valueOf(rowLabel));
	                List<String> seats = new ArrayList<>();

	                int seatNumber = 1;
	                for (int j = 1; j <= columns; j++) {
	                    if (walkwayPositions.contains(j)) {
	                        seats.add(null); // 走道
	                    } else {
	                        seats.add("" + seatNumber); // 座位標籤 (例如 A1, A2)
	                        seatNumber++;
	                    }
	                }

	                row.put("seats", seats);
	                sectionRows.add(row);
	                rowLabel++;
	            }

	            layout.put(sectionName, sectionRows);
	        }

	        return layout;
	    }
}
