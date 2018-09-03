package com.nelioalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nelioalves.cursomc.domain.ProfilePicture;

// Passamos a classe e o tipo do identificador (Integer, no caso)
@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Integer>{
	
}
