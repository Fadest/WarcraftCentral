package com.warcraftcentral.backend.repository;

import com.warcraftcentral.backend.entity.Character;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    Character findCharactersByNameEqualsIgnoreCaseAndRealmSlugAndRegion(String name, String realmSlug, String region);

    List<Character> findCharactersByNameStartingWith(String name);

    List<Character> findCharactersByNameStartingWithAndRealmSlugStartsWith(String name, String realmSlug);
}
