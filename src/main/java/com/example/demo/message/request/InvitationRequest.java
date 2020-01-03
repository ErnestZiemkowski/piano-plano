package com.example.demo.message.request;

public class InvitationRequest {

	private Long id;
	
	private String receiverEmail;
	
	public InvitationRequest() {}
	
	public InvitationRequest(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	
}