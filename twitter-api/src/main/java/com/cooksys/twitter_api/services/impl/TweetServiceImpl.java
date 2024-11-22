package com.cooksys.twitter_api.services.impl;

import com.cooksys.twitter_api.dtos.ContextDto;
import com.cooksys.twitter_api.dtos.CredentialsDto;
import com.cooksys.twitter_api.dtos.HashtagDto;
import com.cooksys.twitter_api.dtos.TweetRequestDto;
import com.cooksys.twitter_api.dtos.TweetResponseDto;
import com.cooksys.twitter_api.dtos.UserResponseDto;
import com.cooksys.twitter_api.entities.Hashtag;
import com.cooksys.twitter_api.entities.Tweet;
import com.cooksys.twitter_api.entities.User;
import com.cooksys.twitter_api.mappers.HashtagMapper;
import com.cooksys.twitter_api.mappers.TweetMapper;
import com.cooksys.twitter_api.mappers.UserMapper;
import com.cooksys.twitter_api.repositories.HashtagRepository;
import com.cooksys.twitter_api.repositories.TweetRepository;
import com.cooksys.twitter_api.repositories.UserRepository;
import com.cooksys.twitter_api.services.TweetService;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;

	@Override
	public List<TweetResponseDto> getAllTweets() {

		return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
	}

	@Override
	public TweetResponseDto getTweetById(Long id) {

		Optional<Tweet> optionalTweets = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweets.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		Tweet tweetToReturn = optionalTweets.get();

		return tweetMapper.entityToDto(tweetToReturn);

	}

	@Override
	public TweetResponseDto deleteTweetById(Long id, TweetRequestDto tweetRequestDto) {
		Optional<Tweet> optionalTweets = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweets.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}
		Tweet tweetToDelete = optionalTweets.get();

		// check if credentials are correct
		User tweetAuthor = tweetToDelete.getAuthor();

		if (tweetAuthor.getCredentials().getPassword()
				.equals(tweetRequestDto.getCredentials().getPassword()) == false) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Login");
		}

		tweetToDelete.setDeleted(true);

		return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToDelete));

	}

	public User getAndVerifyAuthor(TweetRequestDto tweetRequestDto) {
		List<User> allUsers = userRepository.findAll();

		User foundAuthor = null;

		for (User u : allUsers) {

			if ((u.getCredentials().getUsername().equals(tweetRequestDto.getCredentials().getUsername()) == true) && (u
					.getCredentials().getPassword().equals(tweetRequestDto.getCredentials().getPassword()) == true)) {

				foundAuthor = u;

			}
		}

		if (foundAuthor == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
		}

		return foundAuthor;
	}

	public TweetResponseDto processContent(TweetRequestDto tweetRequestDto, Tweet savedTweet) {
		Date date = new Date();
		List<User> allUsers = userRepository.findAll();
		List<Hashtag> allHashtags = hashtagRepository.findAll();

		String[] splitContent = tweetRequestDto.getContent().split(" ");

		LinkedHashSet<String> tempSplitContent = new LinkedHashSet<>();
		for (String C : splitContent) {
			tempSplitContent.add(C);
		}

		List<Hashtag> hashtagsToSave = new ArrayList<>();
		List<User> usersToSave = new ArrayList<>();

		splitContent = tempSplitContent.toArray(new String[tempSplitContent.size()]);// did this to get rid of the
																						// duplicates
		for (String C : splitContent) {
			if (C.charAt(0) == '@') // @ user mentions
			{
				String userName = C.split("@")[1];

				for (User u : allUsers) {
					if (u.getCredentials().getUsername().equals(userName)) {
						u.getMentionedTweets().add(savedTweet);
						usersToSave.add(u);

					}

				}

			} else if (C.charAt(0) == '#') // Hash Tags
			{

				boolean HashtagExsist = false;
				for (Hashtag ht : allHashtags) {
					if (ht.getLabel().equals(C))// if hash tag already exists
					{
						if (ht.getFirstUsed() == null) {
						//	ht.setFirstUsed(new Timestamp(date.getTime()));

						}

					//	ht.setLastUsed(new Timestamp(date.getTime()));
						HashtagExsist = true;
						hashtagsToSave.add(ht);
						savedTweet.getHashtags().add(ht);
					}

				}

				if (HashtagExsist == false) // make a new hashtag
				{
					Hashtag tempHashTag = new Hashtag();
					tempHashTag.setLabel(C);
					//tempHashTag.setFirstUsed(new Timestamp(date.getTime()));
					//tempHashTag.setLastUsed(new Timestamp(date.getTime()));
					ArrayList<Tweet> tempTweets = new ArrayList<Tweet>();
					tempTweets.add(savedTweet);
					tempHashTag.setTweets(tempTweets);

					hashtagsToSave.add(tempHashTag);

					savedTweet.getHashtags().add(tempHashTag);

				}
			}
		}

		hashtagRepository.saveAllAndFlush(hashtagsToSave);
		userRepository.saveAllAndFlush(usersToSave);

		return tweetMapper.entityToDto(savedTweet);
	}

	@Override
	public TweetResponseDto createReplyToTweet(Long id, TweetRequestDto tweetRequestDto) {

		if (tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null
				|| tweetRequestDto.getCredentials().getUsername() == null
				|| tweetRequestDto.getCredentials().getPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Make sure you fill out the content and credential fields");
		}

		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}
		Tweet tweetToReplyTo = optionalTweet.get();

		User foundAuthor = getAndVerifyAuthor(tweetRequestDto);

		Tweet newTweet = new Tweet();

		newTweet.setContent(tweetRequestDto.getContent());
		newTweet.setAuthor(foundAuthor);
		newTweet.setInReplyTo(tweetToReplyTo);

		if (newTweet.getHashtags() == null) {
			newTweet.setHashtags(new ArrayList<Hashtag>());
		}

		Tweet savedTweet = tweetRepository.saveAndFlush(newTweet);

		return processContent(tweetRequestDto, savedTweet);

	}

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {

		if (tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null
				|| tweetRequestDto.getCredentials().getUsername() == null
				|| tweetRequestDto.getCredentials().getPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Make sure you fill out the content and credential fields");
		}

		User foundAuthor = getAndVerifyAuthor(tweetRequestDto);

		Tweet newTweet = new Tweet();

		newTweet.setContent(tweetRequestDto.getContent());
		newTweet.setAuthor(foundAuthor);

		if (newTweet.getHashtags() == null) {
			newTweet.setHashtags(new ArrayList<Hashtag>());
		}

		Tweet savedTweet = tweetRepository.saveAndFlush(newTweet);

		return processContent(tweetRequestDto, savedTweet);

	}

	@Override
	public List<HashtagDto> getTweetTags(Long id) {
		System.out.println("id is "+id);
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		Tweet mainTweet = optionalTweet.get();
		List<Hashtag> tweetHashTags = mainTweet.getHashtags();

		return hashtagMapper.entitiesToDtos(tweetHashTags);

	}

	@Override
	public List<UserResponseDto> getMentionedUsers(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		Tweet mainTweet = optionalTweet.get();

		return userMapper.entitiesToDtos(mainTweet.getMentionedUsers());

	}

	@Override
	public void likeTweet(Long id, CredentialsDto credentialsDto) {
		Optional<Tweet> optionalTweets = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweets.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}
		Tweet tweetToLike = optionalTweets.get();

		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Make sure you fill out all the credential fields");
		}

		List<User> allUsers = userRepository.findAll();

		User foundAuthor = null;

		for (User u : allUsers) {

			if ((u.getCredentials().getUsername().equals(credentialsDto.getUsername()) == true)
					&& (u.getCredentials().getPassword().equals(credentialsDto.getPassword()) == true)) {

				foundAuthor = u;

			}
		}

		if (foundAuthor == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
		}

		foundAuthor.getLikedTweets().add(tweetToLike);
		tweetRepository.saveAndFlush(tweetToLike);

		tweetToLike.getUsersWhoLiked().add(foundAuthor);

		userRepository.saveAndFlush(foundAuthor);

	}

	@Override
	public List<UserResponseDto> getUsersWhoLikedTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		Tweet mainTweet = optionalTweet.get();

		return userMapper.entitiesToDtos(mainTweet.getUsersWhoLiked());

	}

	@Override
	public ContextDto getContextOfTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		Tweet targetTweet = optionalTweet.get();

		ContextDto tempContext = new ContextDto();
		tempContext.setTarget(tweetMapper.entityToDto(targetTweet));

		List<TweetResponseDto> beforeTweets = new ArrayList<>();
		List<TweetResponseDto> afterTweets = new ArrayList<>();

		List<Tweet> allTweets = tweetRepository.findAll();
		// first do before tweets
		Tweet tempTarget = targetTweet;

		for (int i = 0; i < allTweets.size(); i++) {

			if (allTweets.get(i) == tempTarget) {
				if (allTweets.get(i).getInReplyTo() != null) {
					tempTarget = allTweets.get(i).getInReplyTo();
					if (tempTarget.isDeleted() != true) {
						beforeTweets.add(tweetMapper.entityToDto(tempTarget));
					}

					i = 0;
				}

			}

		}
		Collections.reverse(beforeTweets);
		// now do the after tweets
		tempTarget = targetTweet;
		for (int i = 0; i < allTweets.size(); i++) {

			if (allTweets.get(i).getInReplyTo() == tempTarget) {
				tempTarget = allTweets.get(i);
				if (tempTarget.isDeleted() != true) {
					afterTweets.add(tweetMapper.entityToDto(tempTarget));
				}
				i = 0;

			}

		}

		tempContext.setBefore(beforeTweets);
		tempContext.setAfter(afterTweets);

		return tempContext;

	}

	@Override
	public List<TweetResponseDto> getRepliesToTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		Tweet baseTweet = optionalTweet.get();

		List<Tweet> allTweets = tweetRepository.findAll();
		List<Tweet> tweetsThatReplied = new ArrayList<>();

		for (Tweet tw : allTweets) {
			if (tw.getInReplyTo() == baseTweet) {
				tweetsThatReplied.add(tw);
			}

		}

		return tweetMapper.entitiesToDtos(tweetsThatReplied);

	}

	@Override
	public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Make sure you fill out all the credential fields");
		}

		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		List<User> allUsers = userRepository.findAll();
		User foundAuthor = null;

		for (User u : allUsers) {

			if ((u.getCredentials().getUsername().equals(credentialsDto.getUsername()) == true)
					&& (u.getCredentials().getPassword().equals(credentialsDto.getPassword()) == true)) {

				foundAuthor = u;

			}
		}

		if (foundAuthor == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
		}

		Tweet baseTweet = optionalTweet.get();

		Tweet newTweet = new Tweet();
		newTweet.setRepostOf(baseTweet);
		newTweet.setAuthor(foundAuthor);

		Tweet savedTweet = tweetRepository.saveAndFlush(newTweet);

		return tweetMapper.entityToDto(savedTweet);

	}

	@Override
	public List<TweetResponseDto> getRepostOfTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);

		if (optionalTweet.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tweet found with id: " + id);
		}

		Tweet baseTweet = optionalTweet.get();
		List<Tweet> allTweets = tweetRepository.findAll();
		List<Tweet> tweetRepostList = new ArrayList<>();

		for (Tweet tw : allTweets) {
			if (tw.getRepostOf() == baseTweet) {
				tweetRepostList.add(tw);
			}
		}

		return tweetMapper.entitiesToDtos(tweetRepostList);

	}

}
