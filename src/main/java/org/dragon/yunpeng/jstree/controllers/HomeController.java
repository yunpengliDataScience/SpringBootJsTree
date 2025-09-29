package org.dragon.yunpeng.jstree.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String showHomePage() {

		return "symboltree";
	}
	
	@GetMapping("/symboltree2")
	public String showSymbolTree2() {

		return "symboltree2";
	}
	
	@GetMapping("/changeRequest")
	public String showSymbolTree3() {

		return "symboltree3";
	}

	@GetMapping("/demo")
	public String showTree2Page() {

		return "tree";
	}
	
	@GetMapping("/ajtree")
	public String showAlternativeJsonTreePage() {

		return "alternativeJsonTree";
	}

	@GetMapping("/preloadLazyloadTree")
	public String preloadLazyloadTree() {

		return "preloadLazyloadTree";
	}
	
	@GetMapping("/lazyloadTree")
	public String lazyloadTree() {

		return "lazyloadSymbolTree";
	}
}
