package com.ajaysarwade.Treading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaysarwade.Treading.model.AssetModel;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.service.AssetService;
import com.ajaysarwade.Treading.service.UserService;

@RestController
@RequestMapping("/api/asset")
public class AssetController {
	
	@Autowired
	private  AssetService assetService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{assetId}")
	public ResponseEntity<AssetModel> getAssetById(@PathVariable Long assetId) throws Exception{
		AssetModel asset= assetService.getAssetById(assetId);
		return ResponseEntity.ok().body(asset);
	}
	
	@GetMapping("/coin/{coinId}/user")
	public ResponseEntity<AssetModel> getAssetByUserIdAndCoinId(
			@PathVariable String coinId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findProfileByJwt(jwt);
		AssetModel asset= assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
		return new ResponseEntity<>(asset,HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<List<AssetModel>> getAssetForUser(
			@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findProfileByJwt(jwt);
		List<AssetModel> asset= assetService.getUsersAssets(user.getId());
		return  ResponseEntity.ok().body(asset);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
