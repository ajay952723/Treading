package com.ajaysarwade.Treading.service;

import java.util.List;

import com.ajaysarwade.Treading.model.AssetModel;
import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.User;

public interface AssetService {
	
	AssetModel createAsset(User user , CoinMarketData coin,double quantity);
	
	AssetModel getAssetById(Long assetId) throws Exception;
	
	AssetModel getAssetByUserIdAndId(Long userId, Long assetId);
	
	List< AssetModel> getUsersAssets(Long UserId);
	
	AssetModel updateAsset(Long assetId,double quantity) throws Exception;
	
	AssetModel findAssetByUserIdAndCoinId(Long userId, String coinId);
	
	void deleteAsset(Long assetId);

}
