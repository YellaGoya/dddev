package com.d103.dddev.api.issue.util;

import com.d103.dddev.api.issue.model.message.Error;
import com.d103.dddev.api.issue.repository.IssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class IssueUtil {
    private final IssueRepository issueRepository;

    public String unclassified(String parentId, Integer groundId, String type){
        if(parentId == null || parentId.isEmpty()){
            return issueRepository.findByGroundIdAndUnclassifiedAndType(groundId, true, type)
                    .orElseThrow(() -> new NoSuchElementException(Error.NoSuchElementException())).getId();
        }
        return parentId;
    }
}
