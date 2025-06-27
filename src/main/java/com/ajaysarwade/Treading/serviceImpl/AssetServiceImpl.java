package com.ajaysarwade.Treading.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.model.AssetModel;
import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.repository.AssetRepository;
import com.ajaysarwade.Treading.service.AssetService;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	private AssetRepository assetRepository;

	@Override
	public AssetModel createAsset(User user, CoinMarketData coin, double quantity) {
		AssetModel model = new AssetModel();
		model.setUser(user);
		model.setCoin(coin);
		model.setQuantity(quantity);
		model.setBuyPrice(coin.getCurrentPrice());
		return assetRepository.save(model);
	}

	@Override
	public AssetModel getAssetById(Long assetId) throws Exception {

		return assetRepository.findById(assetId).orElseThrow(() -> new Exception("Asset not Foiund"));
	}

	@Override
	public AssetModel getAssetByUserIdAndId(Long userId, Long assetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssetModel> getUsersAssets(Long UserId) {

		return assetRepository.findByUserId(UserId);
	}

	@Override
	public AssetModel updateAsset(Long assetId, double quantity) throws Exception {
		AssetModel oldAsset = getAssetById(assetId);
		oldAsset.setQuantity(quantity + oldAsset.getQuantity());

		return assetRepository.save(oldAsset);
	}

	@Override
	public AssetModel findAssetByUserIdAndCoinId(Long userId, String coinId) {
		return assetRepository.findByUserIdAndCoinId(userId, coinId);
	}

	@Override
	public void deleteAsset(Long assetId) {
		assetRepository.deleteById(assetId);

	}

}
