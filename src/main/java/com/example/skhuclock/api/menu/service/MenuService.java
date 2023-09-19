package com.example.skhuclock.api.menu.service;


import com.example.skhuclock.domain.Menu.Menu;
import com.example.skhuclock.domain.Menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;

    public List<Menu> getMenuData() {
        return menuRepository.findAll();
    }

}
