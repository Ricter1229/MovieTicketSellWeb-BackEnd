package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.MovieBean;
import com.example.demo.dto.FindMovieResponseDTO;
import com.example.demo.dto.FindMovieResponseRecord;
import com.example.demo.dto.MovieResponse;
import com.example.demo.service.MovieService;

@RestController
@CrossOrigin
@RequestMapping("/api/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@PostMapping("/find")
	public FindMovieResponseRecord find(@RequestBody String entity) {
		long count = movieService.count(entity);
		List<FindMovieResponseDTO> products = movieService.find(entity);
		return new FindMovieResponseRecord(count, products, false, "");
	}

//	@GetMapping("/movies/{id}")
//    public MovieResponse findById(@PathVariable Integer id) {
//        List<MovieBean> products = new ArrayList<>();
//        if(id!=null){
//        	MovieBean product = movieService.findById(id);
//            if(product!=null){
//                products.add(product);
//            }
//        }
//        return new MovieResponse(0, products, false, "");
//    }

	@GetMapping("/movies/{id}")
	public FindMovieResponseRecord findById(@PathVariable Integer id) {
		List<FindMovieResponseDTO> products = new ArrayList<>();
		boolean success = false;
		String message = "";

		if (id != null) {
			FindMovieResponseDTO product = movieService.findById(id);
			if (product.getMovie() != null) {
				products.add(product);
				success = true; // 查询成功时设置 success 为 true
			} else {
				message = "未找到电影资料"; // 如果没有找到电影，设置适当的 message
			}
		} else {
			message = "无效的电影 ID"; // 如果 ID 为 null，设置错误信息
		}

		return new FindMovieResponseRecord(0, products, success, message);
	}

	@PostMapping("/addMovie")
	public MovieResponse create(@RequestBody String entity) {
		JSONObject obj = new JSONObject(entity);
		System.out.print(obj.toString());
		String chineseName = obj.isNull("chineseName") ? null : obj.getString("chineseName");
		if (chineseName == null) {
			return new MovieResponse(0, null, false, "電影名是必要欄位");
		} else {
			MovieBean insert = movieService.create(entity);
			if (insert == null) {
				return new MovieResponse(0, null, false, "新增失敗");
			} else {
				return new MovieResponse(0, null, true, "新增成功");
			}
		}
	}

	@PutMapping("/movies/{id}")
	public MovieResponse modify(@PathVariable Integer id, @RequestBody String entity) {

		MovieBean update = movieService.modify(entity);
		System.out.print(entity);
		System.out.print(update);
		if (update == null) {
			return new MovieResponse(0, null, false, "修改失敗");
		} else {
			return new MovieResponse(0, null, true, "修改成功");
		}

	}

	@DeleteMapping("/movies/{id}")
	public MovieResponse remove(@PathVariable Integer id) {
		if (id == null) {
			return new MovieResponse(0, null, false, "Id是必要欄位");
		} else if (!movieService.exists(id)) {
			return new MovieResponse(0, null, false, "Id不存在");
		} else {
			if (movieService.remove(id)) {
				return new MovieResponse(0, null, true, "刪除成功");
			} else {
				return new MovieResponse(0, null, false, "刪除失敗");
			}
		}
	}

}