package com.neulogic.sendit.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neulogic.sendit.models.Parcel;


@Repository
public interface ParcelRepository extends JpaRepository<Parcel,Long>{
	@Query("select p from Parcel p where p.id=?1")
	List<Parcel> findByUserId(Long id);
}
