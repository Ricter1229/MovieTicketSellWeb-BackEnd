package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.AuditoriumBean;
import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreSubPhotoBean;
import com.example.demo.dto.RegionResponse;
import com.example.demo.dto.StoreInnerResponse;
import com.example.demo.dto.StoreOuterResponse;
import com.example.demo.dto.StoreRequestFDto;
import com.example.demo.dto.StoreResponse;
import com.example.demo.dto.api.AuditoriumsDto;
import com.example.demo.dto.api.AuditoriumsDto.AuditoriumDto;
import com.example.demo.dto.api.RegionDto;
import com.example.demo.dto.api.StoreFindDto;
import com.example.demo.dto.api.StoreInnerDto;
import com.example.demo.dto.api.StoreOuterDto;
import com.example.demo.dto.internal.ScheduleInternalDto;
import com.example.demo.repository.StoreSubPhotoRepository;
import com.example.demo.service.AuditoriumScheduleService;
import com.example.demo.service.AuditoriumService;
import com.example.demo.service.StoreService;
import com.example.demo.service.StoreSubPhotoService;
import com.example.demo.util.JsonToSomething;

@CrossOrigin
@RequestMapping("/store")
@RestController
public class StoreController {
	@Autowired
	private StoreService stoService;

	@Autowired
	private StoreSubPhotoService subPhotoService;
	@Autowired
	private StoreSubPhotoRepository storeSubRepo;
	@Autowired
	private AuditoriumScheduleService auditoriumScheduleService;
	@Autowired
	private AuditoriumService audService;
//	傳一個string進來回傳原本的json
//	@PostMapping("/insert")
//	public String storeInsert(@RequestBody String jsonString) {
//		JSONObject json=null;
//		try {
//			json = new JSONObject(jsonString);
//			stoService.updateStore(json);
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return json.toString();
//		
//	}
	// 拆分成store跟subphotos

	@PostMapping("/insert")
	public StoreResponse storeInsert(@RequestBody String jsonString) {
		JSONObject json = null;

//		System.out.println("aaa");
		// 將string的sotre部分轉成json再轉成storebean
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("bbb");
		StoreBean store = JsonToSomething.jsonToStore(json);
//		System.out.println("ccc");
//			stoService.updateStore(store);
		if (stoService.insertStore(store) == null) {
			return new StoreResponse(0, null, false, "新增失敗");
		}

//		System.out.println("ddd");
		// 將string的sotre部分轉成json再轉成List<storesubphotobean>
		List<StoreSubPhotoBean> storeSubPhotos = JsonToSomething.jsonToSubPhotos(json);
		Integer id = stoService.getStoreIdByName(store.getName());
		List<Integer> subIds = storeSubRepo.findStoreSubIdByStoreId(id);
		subPhotoService.deleteStoreSubPhoto(id, subIds);
//		System.out.println("end");
		
		if (subPhotoService.updateStoreSubPhoto(id, storeSubPhotos) == null) {
			return new StoreResponse(0, null, false, "新增失敗");
		}
		
		AuditoriumsDto auditoriumsDto=JsonToSomething.jsonToAuditorium(json);
		
		System.out.println("auditoriumsDto");
		System.out.println(auditoriumsDto);
		if(audService.insertAuditoriumByDto(id, auditoriumsDto)==null) {
			return new StoreResponse(0, null, false, "新增失敗");
		}
		
		

		return new StoreResponse(0, null, true, "修改成功");

	}

	@PutMapping("/update/{id}")
	public StoreResponse modify(@PathVariable Integer id, @RequestBody String json) {
		JSONObject obj;
		try {
			obj = new JSONObject(json);

			// 得到id
			if (obj != null) {
				StoreBean update = stoService.storeModify(obj);
				Integer storeId = obj.isNull("storeId") ? null : obj.getInt("storeId");
				List<Integer> subIds = storeSubRepo.findStoreSubIdByStoreId(id);
				subPhotoService.deleteStoreSubPhoto(id, subIds);
				System.out.println("end");
				List<StoreSubPhotoBean> storeSubPhotos = JsonToSomething.jsonToSubPhotos(obj);
				System.out.println("json");
				subPhotoService.updateStoreSubPhoto(id, storeSubPhotos);
				System.out.println("jsonend");
				List<AuditoriumDto> updateAuditorium = audService.updateAuditorium(id, obj);

					return new StoreResponse(0, null, true, "修改成功");
				
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return new StoreResponse(0, null, false, "修改失敗");

	}

	@DeleteMapping("/movies/{id}")
	public StoreResponse remove(@PathVariable Integer id) {
		if (id == null) {
			return new StoreResponse(0, null, false, "Id是必要欄位");
		} else if (!stoService.exists(id)) {
			return new StoreResponse(0, null, false, "Id不存在");
		} else {
			if (stoService.remove(id)) {
				return new StoreResponse(0, null, true, "刪除成功");
			} else {
				return new StoreResponse(0, null, false, "刪除失敗");
			}
		}
	}

	@PostMapping("/find")
	public StoreResponse find(@RequestBody String entity) {
		long count = stoService.count(entity);
		List<StoreBean> stores = stoService.find(entity);
		List<StoreFindDto> dtos = new ArrayList<>();
		for (StoreBean store : stores) {
			StoreFindDto dto = stoService.setStoreToDto(store);
			dtos.add(dto);
		}
		return new StoreResponse(count, dtos, true, "");
	}

	@PostMapping("/regions/find/{id}")
	public StoreInnerResponse findStoresById(@PathVariable Integer id) {
		StoreBean store = stoService.findById(id);
		StoreInnerDto dto = stoService.setStoreToInnerDto(store);

		return new StoreInnerResponse(1, dto, true, "");
	}

	@PostMapping("/regions/find")
	public StoreOuterResponse findStoresByRegion(@RequestBody String entity) {
		long count = stoService.count(entity);
		List<StoreBean> stores = stoService.find(entity);
		List<StoreOuterDto> dtos = new ArrayList<>();
		for (StoreBean store : stores) {
			StoreOuterDto dto = stoService.setStoreToOuterDto(store);
			dtos.add(dto);
		}
		System.out.println("dtos.size()是");
		System.out.println(dtos.size());
		return new StoreOuterResponse(dtos.size(), dtos, true, "");
	}

	@GetMapping("/regions/findall")
	public RegionResponse findAllRegions() {
		List<Integer> regions = stoService.findRegions();
		List<RegionDto> regionRess = new ArrayList<>();
		if (regions.size() != 0) {
			for (Integer region : regions) {
				RegionDto regionRes = new RegionDto();
				regionRes.setRegionNum(region);
				switch (region) {
				case 1:
					regionRes.setRegion("北部");
					break;
				case 2:
					regionRes.setRegion("中部");
					break;
				case 3:
					regionRes.setRegion("南部");
					break;
				case 4:
					regionRes.setRegion("東部");
					break;
				}
				regionRess.add(regionRes);
			}

		}
		System.out.println("regionRess.size()是");
		System.out.println(regionRess.size());
		return new RegionResponse(regionRess.size(), regionRess, true, "");
	}

	@GetMapping("/find/{id}")
	public StoreResponse findById(@PathVariable Integer id) {
		List<StoreBean> stores = new ArrayList<>();
		if (id != null) {
			StoreBean store = stoService.findById(id);
			if (store != null) {
				stores.add(store);
			}
		}
		List<StoreFindDto> dtos = new ArrayList<>();
		for (StoreBean store : stores) {
			List<StoreFindDto.AuditoriumDto> auditoriumList=new ArrayList<>();
			StoreFindDto dto = stoService.setStoreToDto(store);
			dtos.add(dto);
			List<AuditoriumBean> auditoriumBeans = store.getAuditoriumBeans();
			for (AuditoriumBean auditoriumBean : auditoriumBeans) {
				StoreFindDto.AuditoriumDto auditoriumDto=new StoreFindDto.AuditoriumDto();
				auditoriumDto.setAuditoriumId(auditoriumBean.getId().toString());
				auditoriumDto.setAuditoriumName(auditoriumBean.getName());
				auditoriumList.add(auditoriumDto);
			}
			dto.setAuditoriumList(auditoriumList);
		}
		return new StoreResponse(dtos.size(), dtos, true, "");
	}

	@DeleteMapping("/subphoto/delete/{id}")
	public StoreResponse removeSubPhoto(@PathVariable Integer id) {
		if (id == null) {
			return new StoreResponse(0, null, false, "Id是必要欄位");
		} else if (!subPhotoService.exists(id)) {
			return new StoreResponse(0, null, false, "Id不存在");
		} else {
			if (subPhotoService.deleteSubPhoto(id)) {
				return new StoreResponse(0, null, true, "刪除成功");
			} else {
				return new StoreResponse(0, null, false, "刪除失敗");
			}
		}
	}
	
	@PostMapping("/schedules")
    public ApiResponse<List<ScheduleInternalDto>> getSchedules(@RequestBody StoreRequestFDto request) {
        List<ScheduleInternalDto> schedules = auditoriumScheduleService.getSchedulesByStoreIdAndDateRange(request.getStoreId(), request.getMovieId());
        return ApiResponse.success(schedules);
    }
}
