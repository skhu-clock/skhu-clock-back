package com.example.skhuclock.api.menu.controller;

import com.example.skhuclock.api.menu.service.MenuService;
import com.example.skhuclock.domain.Menu.Menu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/menus")
@Slf4j
@Tag(name = "Menu", description = "국수나무 메뉴 API Document")
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    @Operation(summary = "국수나무 메뉴 조회 json", description = "국수나무 메뉴 정보 출력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Menu.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    public ResponseEntity<Menu> getAllMenus() {
        return new ResponseEntity(menuService.getMenuData(),
                HttpStatus.OK);
    }
}
