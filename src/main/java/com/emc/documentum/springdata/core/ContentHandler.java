//package com.emc.documentum.springdata.core;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class ContentHandler {
//
//	@Before("@annotation(com.emc.documentum.springdata.entitymanager.annotations.Content) && "
//			+ "execution(* set*(..))")
//	public void setData(JoinPoint jp) {
//		Object obj = jp.getThis();
//	}
//	
//	@Before("@annotation(com.emc.documentum.springdata.entitymanager.annotations.Content) && "
//			+ "execution(* get*(..))")
//	public void getData(JoinPoint jp) {
//		Object obj = jp.getThis();
//
//	}
//
//	
//	
//	@Before("execution(public * *(..))")
//	public void check() {
//		System.out.println("Hello from the aspect");
//	}
//	
//}

