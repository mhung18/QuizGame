package com.example.quizzone;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.example.quizzone.Model.CategoryModel;
import com.example.quizzone.Model.FillInWordModel;
import com.example.quizzone.Model.FlashCardModel;
import com.example.quizzone.Model.ProfileModel;
import com.example.quizzone.Model.QuestionModel;
import com.example.quizzone.Model.RankModel;
import com.example.quizzone.Model.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {
    public static FirebaseFirestore g_firestore;
    public static List<CategoryModel> g_catList = new ArrayList<>();
    public static int g_selected_cat_index = 0;

    public static List<TestModel> g_testList = new ArrayList<>();
    public static int g_selected_test_index = 0;

    public static List<FlashCardModel> g_flashCardList = new ArrayList<>();

    public static List<FillInWordModel> g_fillInWordList = new ArrayList<>();

    public static List<QuestionModel> g_quesList = new ArrayList<>();

    public static List<RankModel> g_userList = new ArrayList<>();
    public static int g_userCount = 0;
    public static boolean isMeOnTopList = false;

    public static ProfileModel myProfile = new ProfileModel("NA",null,null);

    public static RankModel myPerformance = new RankModel("NULL",0,-1);

    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;

    public static void createUserData(String email, String name, MyCompleteListener completeListener){
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL_ID",email);
        userData.put("NAME",name);
        userData.put("TOTAL_SCORE",0);

        DocumentReference userDoc = g_firestore.collection("USER").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc,userData);

        DocumentReference countDoc = g_firestore.collection("USER").document("TOTAL_USER");
        batch.update(countDoc,"COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void saveProfileData(String name, String phone, MyCompleteListener completeListener){
        Map<String, Object> profileData = new ArrayMap<>();

        profileData.put("NAME", name);
        if (phone != null){
            profileData.put("PHONE", phone);
        }

        g_firestore.collection("USER").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        if(phone != null){
                            myProfile.setPhone(phone);
                        }
                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void getUserData(final MyCompleteListener completeListener){
        g_firestore.collection("USER").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("NAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                        if(documentSnapshot.getString("PHONE") != null){
                            myProfile.setPhone(documentSnapshot.getString("PHONE"));
                        }
                        myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                        myPerformance.setName(documentSnapshot.getString("NAME"));
                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void getTopUsers(MyCompleteListener completeListener){
        g_userList.clear();

        String myUID = FirebaseAuth.getInstance().getUid();

        g_firestore.collection("USER")
                .whereGreaterThan("TOTAL_SCORE",0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            g_userList.add(new RankModel(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));

                            if(myUID.compareTo(doc.getId()) == 0){
                                isMeOnTopList = true;
                                myPerformance.setRank(rank);
                            }

                            rank++;
                        }

                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void getUsersCount(MyCompleteListener completeListener){
        g_firestore.collection("USER").document("TOTAL_USER")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_userCount = documentSnapshot.getLong("COUNT").intValue();
                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void loadCategories(final MyCompleteListener completeListener){
        g_catList.clear();

        g_firestore.collection("QUIZ")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            docList.put(doc.getId(),doc);
                        }

                        QueryDocumentSnapshot catListDoc = docList.get("Categories");

                        long catCount = catListDoc.getLong("COUNT");

                        for(int i = 1;i <= catCount;i++){
                            String catId = catListDoc.getString("CAT" + String.valueOf(i) + "_ID");
                            QueryDocumentSnapshot catDoc = docList.get(catId);

                            int noOfTest = catDoc.getLong("NO_OF_TEST").intValue();
                            String catName = catDoc.getString("NAME");
                            String type = catDoc.getString("TYPE");
                            g_catList.add(new CategoryModel(catId,catName,noOfTest,type));
                        }

                        completeListener.OnSuccess();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void loadQuestion(MyCompleteListener completeListener){
        g_quesList.clear();
        g_flashCardList.clear();
        g_fillInWordList.clear();

        String type = getTypeOfQuiz(g_selected_cat_index);
        if(type.equals("mc")){
            g_firestore.collection("Questions")
                    .whereEqualTo("CATEGORY",g_catList.get(g_selected_cat_index).getDocID())
                    .whereEqualTo("TEST",g_testList.get(g_selected_test_index).getTestID())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots){
                                g_quesList.add(new QuestionModel(
                                        doc.getString("QUESTION"),
                                        doc.getString("A"),
                                        doc.getString("B"),
                                        doc.getString("C"),
                                        doc.getString("D"),
                                        doc.getLong("ANSWER").intValue(),
                                        -1,
                                        NOT_VISITED
                                ));
                            }
                            completeListener.OnSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completeListener.OnFailure();
                        }
                    });
        } else if (type.equals("fc")) {
            g_firestore.collection("FlashCards")
                    .whereEqualTo("CATEGORY",g_catList.get(g_selected_cat_index).getDocID())
                    .whereEqualTo("TEST",g_testList.get(g_selected_test_index).getTestID())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots){
                                g_flashCardList.add(new FlashCardModel(
                                        doc.getString("QUESTION"),
                                        doc.getString("TRANSLATE"),
                                        NOT_VISITED
                                ));
                            }
                            completeListener.OnSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completeListener.OnFailure();
                        }
                    });
        } else if (type.equals("fiw")){
            g_firestore.collection("FillInWord")
                    .whereEqualTo("CATEGORY",g_catList.get(g_selected_cat_index).getDocID())
                    .whereEqualTo("TEST",g_testList.get(g_selected_test_index).getTestID())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots){
                                g_fillInWordList.add(new FillInWordModel(
                                        doc.getString("HINT"),
                                        "NULL",
                                        doc.getString("ANSWER"),
                                        NOT_VISITED
                                ));
                            }
                            completeListener.OnSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completeListener.OnFailure();
                        }
                    });
        }


    }

    public static void loadTestData (final MyCompleteListener completeListener){
        g_testList.clear();

        g_firestore.collection("QUIZ").document(g_catList.get(g_selected_cat_index).getDocID())
                .collection("TESTS_LIST").document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTest = g_catList.get(g_selected_cat_index).getNoOfTests();
                        for(int i=1;i <= noOfTest;i++){
                            String testId = documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID");
                            String testTopic = documentSnapshot.getString("TEST" + String.valueOf(i) + "_TOPIC");
                            Long testTime = documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME");
                            if (testTime != null) {
                                g_testList.add(new TestModel(testId, testTopic,0, testTime.intValue()));
                            } else {
                                g_testList.add(new TestModel(testId, testTopic,0, 0));
                            }
                        }
                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void loadMyScores(MyCompleteListener completeListener){
        g_firestore.collection("USER").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(int i = 0;i < g_testList.size();i++){
                            int top = 0;
                            if(documentSnapshot.get(g_testList.get(i).getTestID()) != null){
                                top = documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                            }
                            g_testList.get(i).setTopScore(top);
                        }
                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void saveResult(int finalScore, MyCompleteListener completeListener){
        WriteBatch batch = g_firestore.batch();

        DocumentReference userDoc = g_firestore.collection("USER").document(FirebaseAuth.getInstance().getUid());
        batch.update(userDoc,"TOTAL_SCORE",finalScore);

        if(finalScore > g_testList.get(g_selected_test_index).getTopScore()){
            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");

            Map<String,Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getTestID(), finalScore);

            batch.set(scoreDoc,testData, SetOptions.merge());
        }
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(finalScore > g_testList.get(g_selected_test_index).getTopScore()){
                            g_testList.get(g_selected_test_index).setTopScore(finalScore);
                        }
                        myPerformance.setScore(finalScore);
                        completeListener.OnSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static String getTopicOfQuiz(int g_selected_test_index){
        String topic = g_testList.get(g_selected_test_index).getTopic();
        return topic;
    }

    public static String getTypeOfQuiz(int g_selected_cat_index){
        String type = g_catList.get(g_selected_cat_index).getType();
        return type;
    }

    public static void loadData(MyCompleteListener completeListener){
        loadCategories(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void OnSuccess() {
                        getUsersCount(completeListener);
                    }

                    @Override
                    public void OnFailure() {
                        completeListener.OnFailure();
                    }
                });
            }

            @Override
            public void OnFailure() {
                completeListener.OnFailure();
            }
        });
    }
}
