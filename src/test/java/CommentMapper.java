import com.wenda.communicationsystem.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
    List<Comment> selectCommentByEntry(int entryId, int entryType);

    int getCommentCount(int entryId, int entryType);

    int updateStatus(int id, int status);
}