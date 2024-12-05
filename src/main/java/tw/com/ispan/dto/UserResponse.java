package tw.com.ispan.dto;

import java.util.List;

import tw.com.ispan.domain.MemberBean;

public record UserResponse(long count, List<MemberBean> list) {
    
}
