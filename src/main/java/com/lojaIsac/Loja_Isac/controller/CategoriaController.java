package com.lojaIsac.Loja_Isac.controller;

import com.lojaIsac.Loja_Isac.model.Categoria;
import com.lojaIsac.Loja_Isac.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;

@Controller
@RequestMapping("/administrativo/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(Categoria categoria) {
        ModelAndView mv = new ModelAndView("administrativo/categorias/cadastro");
        return mv.addObject("categoria", categoria);
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/administrativo/categorias/lista");
        return mv.addObject("listaCategorias", categoriaRepository.findAll());
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return cadastrar(categoria.get());
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        categoriaRepository.delete(categoria.get());
        return listar();
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@Validated Categoria categoria, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(categoria);
        }
        categoriaRepository.saveAndFlush(categoria);

        return cadastrar(new Categoria());
    }

}
