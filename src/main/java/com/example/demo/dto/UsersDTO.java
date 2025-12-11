package com.example.demo.dto;

import com.example.demo.entity.Users;
import com.example.demo.enums.Role;

public class UsersDTO {
    private String usernameDTO;
    private String emailDTO;
    private Role role;
    private boolean lockedDTO;
    private boolean isBlockedDTO;
    private long idDTO;
private String whyBlockedDTO;

    public UsersDTO(Users user) {
        this.usernameDTO = user.getRealUsername();
        this.isBlockedDTO = user.getIsBlocked();
        this.emailDTO = user.getEmail();
        this.role = user.getRole();
        this.lockedDTO = user.isLocked();
        this.idDTO = user.getId();
        this.whyBlockedDTO = user.getWhyBlocked();
    }

    public UsersDTO() {
    }

    public String getUsernameDTO() {
        return usernameDTO;
    }

    public void setUsernameDTO(String usernameDTO) {
        this.usernameDTO = usernameDTO;
    }

    public String getEmailDTO() {
        return emailDTO;
    }

    public void setEmailDTO(String emailDTO) {
        this.emailDTO = emailDTO;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isLockedDTO() {
        return lockedDTO;
    }

    public void setLockedDTO(boolean lockedDTO) {
        this.lockedDTO = lockedDTO;
    }

    public boolean isBlockedDTO() {
        return isBlockedDTO;
    }

    public void setBlockedDTO(boolean blockedDTO) {
        isBlockedDTO = blockedDTO;
    }

    public long getIdDTO() {
        return idDTO;
    }

    public void setIdDTO(long idDTO) {
        this.idDTO = idDTO;
    }

    public String getWhyBlockedDTO() {
        return whyBlockedDTO;
    }

    public void setWhyBlockedDTO(String whyBlockedDTO) {
        this.whyBlockedDTO = whyBlockedDTO;
    }
}
