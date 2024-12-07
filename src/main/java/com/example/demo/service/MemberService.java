package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.MemberBean;
import com.example.demo.repository.MemberRepository;

@Service
@Transactional
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;

	public String registerTemp(MemberBean newMember) {
        // 帳號是否存在
        boolean exists = memberRepository.existsByAccountOrEmail(newMember.getAccount(), newMember.getEmail());
        if (exists) {
            return "用戶已存在";
        }

        return "Registration successful!";
    }

    public String register(MemberBean newMember) {
        // 加入新帳號
        memberRepository.save(newMember);
        return "註冊成功!";
    }

	public MemberBean login(String account, String password) {
		if (account != null) {
            Optional<MemberBean> optional = memberRepository.findByAccount(account);

            if (optional.isPresent()) {
                MemberBean bean = optional.get();
                if (password != null && !password.isEmpty()) {
                    String pass = bean.getPassword();
                    if (pass.equals(password)) {
                        return bean;
                    }
                }
            }
        }

		return null;
	}
    
    public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
            return memberRepository.count(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

    public List<MemberBean> find(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return memberRepository.find(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public MemberBean findByAccount(String account) {
        Optional<MemberBean> memberOptional = memberRepository.findByAccount(account);
        return memberOptional.orElse(null);
    }
    
    public void updatePassword(String email, String newPassword) {
        Optional<MemberBean> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            MemberBean member = memberOptional.get();
            member.setPassword(newPassword);
            memberRepository.save(member);
        }
    }
}
