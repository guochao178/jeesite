/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.test.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.ResponseUtil;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.test.entity.Test;
import com.thinkgem.jeesite.modules.test.service.TestService;
import com.thinkgem.jeesite.modules.test.entity.LitemallCart;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 测试Controller
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "${adminPath}/test/test")
public class TestController extends BaseController {

	@Autowired
	private TestService testService;
	
	@ModelAttribute
	public Test get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return testService.get(id);
		}else{
			return new Test();
		}
	}
	
	/**
	 * 显示列表页
	 * @param test
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("test:test:view")
	@RequestMapping(value = {"list", ""})
	public String list(Test test, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/test/testList";
	}
	
	/**
	 * 获取硕正列表数据（JSON）
	 * @param test
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("test:test:view")
	@RequiresPermissions("user")
	@RequestMapping(value = "listData")
	@ResponseBody
	//public Page<Test> listData(Test test, HttpServletRequest request, HttpServletResponse response, Model model) {
	public Object listData(Test test, HttpServletRequest request, HttpServletResponse response, Model model) {
/*		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			test.setCreateBy(user);
		}
        Page<Test> page = testService.findPage(new Page<Test>(request, response), test); 
        return page;*/
        
        List<LitemallCart> cartList = new ArrayList();//cartService.queryByUid(userId);
        for (int i =0; i<10; i++) {
        	short num = 1;
        	LitemallCart e = new LitemallCart();
        	e.setProductId(i);
        	e.setGoodsName("天通" + i);
        	e.setRetailPrice(new BigDecimal("1"));
        	e.setNumber(num);
        	e.setChecked(false);
        	cartList.add(e);
        }
        Integer goodsCount = 0;
        BigDecimal goodsAmount = new BigDecimal(0.00);
        Integer checkedGoodsCount = 0;
        BigDecimal checkedGoodsAmount = new BigDecimal(0.00);
        for (LitemallCart cart : cartList) {
            goodsCount += cart.getNumber();
            goodsAmount = goodsAmount.add(cart.getRetailPrice().multiply(new BigDecimal(cart.getNumber())));
            if (cart.getChecked()) {
                checkedGoodsCount += cart.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cart.getRetailPrice().multiply(new BigDecimal(cart.getNumber())));
            }
        }
        Map<String, Object> cartTotal = new HashMap<String, Object>();
        cartTotal.put("goodsCount", goodsCount);
        cartTotal.put("goodsAmount", goodsAmount);
        cartTotal.put("checkedGoodsCount", checkedGoodsCount);
        cartTotal.put("checkedGoodsAmount", checkedGoodsAmount);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("cartList", cartList);
        result.put("cartTotal", cartTotal);

        return ResponseUtil.ok(result);        
	}
	
	/**
	 * 新建或修改表单
	 * @param test
	 * @param model
	 * @return
	 */
	@RequiresPermissions("test:test:view")
	@RequestMapping(value = "form")
	public String form(Test test, Model model) {
		model.addAttribute("test", test);
		return "modules/test/testForm";
	}

	/**
	 * 表单保存方法
	 * @param test
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("test:test:edit")
	@RequestMapping(value = "save")
	public String save(Test test, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, test)){
			return form(test, model);
		}
//		testService.save(test);
		addMessage(redirectAttributes, "保存测试'" + test.getName() + "'成功");
		return "redirect:" + adminPath + "/test/test/?repage";
	}
	
	/**
	 * 删除数据方法
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("test:test:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Test test, RedirectAttributes redirectAttributes) {
//		testService.delete(test);
//		addMessage(redirectAttributes, "删除测试成功");
//		return "redirect:" + adminPath + "/test/test/?repage";
		return "true";
	}

}
