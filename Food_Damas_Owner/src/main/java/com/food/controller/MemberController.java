package com.food.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.food.domain.MemberVO;
import com.food.services.Member.MemberService;
import com.food.util.UploadFileUtils;

@Controller
@RequestMapping("/")
public class MemberController {
	
	@Resource(name="uploadPathRegister")
	private String uploadPathRegister;
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Inject
	private MemberService service;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void ft_registergGET(MemberVO member, Model model) throws Exception {

		logger.info("회원가입이느니라!!!");
	}
	
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ModelAndView ft_registerPOST(MemberVO vo /*, RedirectAttributes rttr*/) throws Exception {
		logger.info("푸드트럭 회원가입");
		
		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView("login"));
		
		MultipartFile file1 = vo.getFile1();
		MultipartFile file2 = vo.getFile2();
		
		if(file1 !=null){
			logger.info("원래파일이름 :" + file1.getOriginalFilename());
			logger.info("사이즈: " + file1.getSize());
			logger.info("타입: " + file1.getContentType());
		}else if (file2 != null){
			logger.info("원래파일이름 :" + file2.getOriginalFilename());
			logger.info("사이즈: " + file2.getSize());
			logger.info("타입: " + file2.getContentType());
		}
		logger.info("---------------------------------------");
		String u_profile_img = UploadFileUtils.uploadFile(uploadPathRegister, file1.getOriginalFilename(), file1.getBytes());
		String co_doc = UploadFileUtils.uploadFile(uploadPathRegister, file2.getOriginalFilename(), file2.getBytes());
		logger.info(u_profile_img);
		logger.info(co_doc);
		vo.setU_profile_img(u_profile_img);
		vo.setCo_doc(co_doc);
		
		logger.info(vo.toString());
		service.insert(vo);
	/*	rttr.addFlashAttribute("result", "success");*/
		
		return mav;
	}
	
	@ResponseBody
    @RequestMapping(value = "/idcheck", method = RequestMethod.POST)
	public String idcheck(@RequestBody String u_id) throws Exception {
		logger.info("여기는오삼?");
	     logger.info(""+service.idcheck(u_id));
	     return service.idcheck(u_id);
	}

	
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modifyGET(Model model, String u_id)throws Exception{
		logger.info("회원가입 수정 화면");
		model.addAttribute("modify", service.view("food1"));
		 logger.info(""+service.view("food1"));
		return "modify";
		
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modifyPOST(MemberVO vo)throws Exception{
		logger.info("회원가입 수정 (update)할꺼에여");
		
			MultipartFile file = vo.getFile1();
		
		if(file != null){
			//파일다운로드 해줄꺼에요
			String u_profile_img = UploadFileUtils.uploadFile(uploadPathRegister, file.getOriginalFilename(), file.getBytes());
			//파일이 있으면, 들어간 파일 정보 알려주고, 이미지 값 넣어준다
				logger.info("원래파일이름 :" + file.getOriginalFilename());
				logger.info("사이즈: " + file.getSize());
				logger.info("타입: " + file.getContentType());
				vo.setU_profile_img(u_profile_img);
				logger.info(u_profile_img);
		}
		logger.info(vo.toString());
		service.update(vo);
	}
	

}

