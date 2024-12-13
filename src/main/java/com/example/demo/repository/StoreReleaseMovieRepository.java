package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.StoreReleaseMovieBean;
import com.example.demo.domain.VersionBean;
import com.example.demo.dto.internal.StoreInternalDto;

public interface StoreReleaseMovieRepository extends JpaRepository<StoreReleaseMovieBean, Integer> {

	List<StoreReleaseMovieBean> findByStoreId(Integer storeId);

	@Query("SELECT srm " + "FROM StoreReleaseMovieBean srm " + "JOIN MovieVersionBean mv ON srm.movieVersionId = mv.id "
			+ "WHERE srm.storeId = :storeId " + "AND mv.versionId = :versionId")
	List<StoreReleaseMovieBean> findByStoreIdAndVersionId(@Param("storeId") Integer storeId,
			@Param("versionId") Integer versionId);
	
    @Query("SELECT DISTINCT new com.example.demo.domain.VersionBean(v.id, v.version) " +
            "FROM StoreReleaseMovieBean srm " +
            "JOIN MovieVersionBean mv ON srm.movieVersionId = mv.id " +
            "JOIN VersionBean v ON mv.versionId = v.id " +
            "WHERE srm.storeId = :storeId")
    List<VersionBean> findStoreReleaseMovieVersions(@Param("storeId") Integer storeId);

	List<StoreReleaseMovieBean> findByMovieVersionId(Integer id);

}
