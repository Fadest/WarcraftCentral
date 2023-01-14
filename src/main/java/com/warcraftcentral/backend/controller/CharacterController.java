package com.warcraftcentral.backend.controller;

import com.warcraftcentral.backend.entity.Character;
import com.warcraftcentral.backend.model.CharacterDTO;
import com.warcraftcentral.backend.service.CharacterService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ControllerAdvice
public class CharacterController {
    private final CharacterService characterService;

    @GetMapping("/characters/search")
    @ResponseBody
    public List<CharacterDTO> searchCharacters(@RequestParam("query") String query) {
        return this.characterService.getCharacters(query);
    }

    @GetMapping("/characters/{region}/{realm}/{name}")
    @ResponseBody
    public Character getCharacter(
        @PathVariable("name") String name,
        @PathVariable("realm") String realm,
        @PathVariable("region") String region,
        @RequestParam(value = "update", required = false, defaultValue = "false") boolean update
    ) {
        return this.characterService.getCharacter(name, realm, region, update);
    }
}
