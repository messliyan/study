package com.ruoyi.basis.util;

import com.ruoyi.basis.domain.QueStore;
import com.ruoyi.common.core.utils.StringUtils;
import com.yrot.business.api.domain.store.Question;
import com.yrot.business.api.domain.store.QuestionOption;
import com.yrot.business.api.enums.OptionsMap;
import com.yrot.business.api.enums.QuestionType;
import java.util.ArrayList;

/***
 * @author jiabin
 * @date 2021-04-19 15:46:36
 **/
public class QuestionFactory {

  public static Question createQuestionBank(QueStore queStore) {

    return new Question() {{
      setQuestionType(QuestionType.getEnumForDescription(queStore.getStoreType()));
      setQuestionID(queStore.getStoreId() + "");
      setAnalysis(queStore.getStoreDetail().getParsing());
      setAnswer("");
      setFldAnswer(OptionsMap.getFldAnswerForOptions(queStore.getStoreDetail().getAnswer()));
      setFldName(queStore.getStoreContent());
      setQuestionOption(new ArrayList<QuestionOption>(){{
        if (StringUtils.isNotEmpty(queStore.getStoreDetail().getSelectionA())){
          add(new QuestionOption(){{
           setFldOptionIndex(OptionsMap.getEnumForDescription("A").getCode());
           setFldOptionText(queStore.getStoreDetail().getSelectionA());
          }});
        }
        if (StringUtils.isNotEmpty(queStore.getStoreDetail().getSelectionB())){
          add(new QuestionOption(){{
            setFldOptionIndex(OptionsMap.getEnumForDescription("B").getCode());
            setFldOptionText(queStore.getStoreDetail().getSelectionB());
          }});
        }
        if (StringUtils.isNotEmpty(queStore.getStoreDetail().getSelectionC())){
          add(new QuestionOption(){{
            setFldOptionIndex(OptionsMap.getEnumForDescription("C").getCode());
            setFldOptionText(queStore.getStoreDetail().getSelectionC());
          }});
        }
        if (StringUtils.isNotEmpty(queStore.getStoreDetail().getSelectionD())) {
          add(new QuestionOption() {{
            setFldOptionIndex(OptionsMap.getEnumForDescription("D").getCode());
            setFldOptionText(queStore.getStoreDetail().getSelectionD());
          }});
        }
        if (StringUtils.isNotEmpty(queStore.getStoreDetail().getSelectionE())) {
          add(new QuestionOption(){{
           setFldOptionIndex(OptionsMap.getEnumForDescription("E").getCode());
           setFldOptionText(queStore.getStoreDetail().getSelectionE());
          }});
        }
        if (StringUtils.isNotEmpty(queStore.getStoreDetail().getSelectionF())) {
          add(new QuestionOption() {{
            setFldOptionIndex(OptionsMap.getEnumForDescription("F").getCode());
            setFldOptionText(queStore.getStoreDetail().getSelectionF());
          }});
        }
        }}
      );
    }};
  }
}
