package com.booleanuk.api.models;

import lombok.Data;

@Data


public class LikeRequest {
	private int userId;
	private int messageId;
}
