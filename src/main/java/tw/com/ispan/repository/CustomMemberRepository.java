package tw.com.ispan.repository;

import java.util.List;
import org.json.JSONObject;

import tw.com.ispan.domain.MemberBean;

public interface CustomMemberRepository {
    List<MemberBean> find(JSONObject obj);
    
    long count(JSONObject obj);
}