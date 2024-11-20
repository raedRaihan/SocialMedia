package com.cooksys.twitter_api.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cooksys.lemonadestand.exceptions.NotFoundException;
import com.cooksys.twitter_api.dtos.ErrorDto;
import com.cooksys.twitter_api.exceptions.BadRequestException;
import com.cooksys.twitter_api.exceptions.NotAuthorizedException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = {"com.cooksys.twitter_api.controllers"})
@ResponseBody
public class TwitterApiApplicationControllerAdvice {
	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException)
	{
		return new ErrorDto(badRequestException.getMessage());
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException notFoundException)
	{
		return new ErrorDto(notFoundException.getMessage());
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(NotAuthorizedException.class)
	public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotAuthorizedException notAuthorizedException)
	{
		return new ErrorDto(notAuthorizedException.getMessage());
	}
	
	
	

}
