package com.lojaIsac.Loja_Isac.controller;

import com.lojaIsac.Loja_Isac.model.Funcionario;
import com.lojaIsac.Loja_Isac.repository.CidadeRepository;
import com.lojaIsac.Loja_Isac.repository.FuncionarioRepository;
import com.lojaIsac.Loja_Isac.service.EnviarEmailService;
import com.lojaIsac.Loja_Isac.validacaoCPF.ValidarCPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Controller
@RequestMapping("/administrativo/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepositorio;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EnviarEmailService enviarEmailService;

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(Funcionario funcionario) {
        ModelAndView mv = new ModelAndView("administrativo/funcionarios/cadastro");
        mv.addObject("funcionario", funcionario);
        mv.addObject("listaCidades", cidadeRepository.findAll());
        return mv;
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/administrativo/funcionarios/lista");
        mv.addObject("listaFuncionarios", funcionarioRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioRepositorio.findById(id);
        return cadastrar(funcionario.get());
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioRepositorio.findById(id);
        funcionarioRepositorio.delete(funcionario.get());
        return listar();
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@Validated Funcionario funcionario, BindingResult result) {
            if (result.hasErrors()) {
                return cadastrar(funcionario);
            }
            funcionario.setSenha(new BCryptPasswordEncoder().encode(funcionario.getSenha()));
            funcionarioRepositorio.saveAndFlush(funcionario);
            enviarEmailService.enviar(funcionario.getEmail(), "Dados Cadastrais", "A sua senha cadastrada foi "+funcionario.getSenha());
            return cadastrar(new Funcionario());
    }

}