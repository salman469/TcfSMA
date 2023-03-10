package com.tcf.sma.Helpers.DbTables.Expense;

import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseAmountClosingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseHeadsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSchoolPettyCashLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSchoolPettyCashMonthlyLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubHeadsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadExceptionLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadLimitsModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseSubheadLimitsMonthlyModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionBucketModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionCategoryModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionFlowModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionImagesModel;
import com.tcf.sma.Models.RetrofitModels.Expense.ExpenseTransactionModel;

import java.util.ArrayList;

public class ExpenseDataResponseModel {
    public ArrayList<ExpenseHeadsModel> Heads;
    public ArrayList<ExpenseSubHeadsModel> Subheads;
    public ArrayList<ExpenseSubheadLimitsModel> SubheadLimits;
    public ArrayList<ExpenseSubheadLimitsMonthlyModel> SubheadLimitsMonthly;
    public ArrayList<ExpenseSchoolPettyCashLimitsModel> SchoolPettyCashLimits;
    public ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> SchoolPettyCashMonthlyLimits;
    public ArrayList<ExpenseSubheadExceptionLimitsModel> SubheadExceptionLimits;
    public ArrayList<ExpenseTransactionModel> Transactions;
    public ArrayList<ExpenseTransactionImagesModel> TransactionImages;
    public ArrayList<ExpenseTransactionFlowModel> TransactionFlow;
    public ArrayList<ExpenseTransactionBucketModel> TransactionBucket;
    public ArrayList<ExpenseTransactionCategoryModel> TransactionCategory;
    public ArrayList<ExpenseAmountClosingModel> AmountClosing;

    public ArrayList<ExpenseHeadsModel> getHeads() {
        return Heads;
    }

    public void setHeads(ArrayList<ExpenseHeadsModel> heads) {
        Heads = heads;
    }

    public ArrayList<ExpenseSubHeadsModel> getSubheads() {
        return Subheads;
    }

    public void setSubheads(ArrayList<ExpenseSubHeadsModel> subheads) {
        Subheads = subheads;
    }

    public ArrayList<ExpenseSubheadLimitsModel> getSubheadLimits() {
        return SubheadLimits;
    }

    public void setSubheadLimits(ArrayList<ExpenseSubheadLimitsModel> subheadLimits) {
        SubheadLimits = subheadLimits;
    }

    public ArrayList<ExpenseSubheadLimitsMonthlyModel> getSubheadLimitsMonthly() {
        return SubheadLimitsMonthly;
    }

    public void setSubheadLimitsMonthly(ArrayList<ExpenseSubheadLimitsMonthlyModel> subheadLimitsMonthly) {
        SubheadLimitsMonthly = subheadLimitsMonthly;
    }

    public ArrayList<ExpenseSchoolPettyCashLimitsModel> getSchoolPettyCashLimits() {
        return SchoolPettyCashLimits;
    }

    public void setSchoolPettyCashLimits(ArrayList<ExpenseSchoolPettyCashLimitsModel> schoolPettyCashLimits) {
        SchoolPettyCashLimits = schoolPettyCashLimits;
    }

    public ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> getSchoolPettyCashMonthlyLimits() {
        return SchoolPettyCashMonthlyLimits;
    }

    public void setSchoolPettyCashMonthlyLimits(ArrayList<ExpenseSchoolPettyCashMonthlyLimitsModel> schoolPettyCashMonthlyLimits) {
        SchoolPettyCashMonthlyLimits = schoolPettyCashMonthlyLimits;
    }

    public ArrayList<ExpenseSubheadExceptionLimitsModel> getSubheadExceptionLimits() {
        return SubheadExceptionLimits;
    }

    public void setSubheadExceptionLimits(ArrayList<ExpenseSubheadExceptionLimitsModel> subheadExceptionLimits) {
        SubheadExceptionLimits = subheadExceptionLimits;
    }

    public ArrayList<ExpenseTransactionFlowModel> getTransactionFlow() {
        return TransactionFlow;
    }

    public void setTransactionFlow(ArrayList<ExpenseTransactionFlowModel> transactionFlow) {
        TransactionFlow = transactionFlow;
    }

    public ArrayList<ExpenseTransactionBucketModel> getTransactionBucket() {
        return TransactionBucket;
    }

    public void setTransactionBucket(ArrayList<ExpenseTransactionBucketModel> transactionBucket) {
        TransactionBucket = transactionBucket;
    }

    public ArrayList<ExpenseTransactionCategoryModel> getTransactionCategory() {
        return TransactionCategory;
    }

    public void setTransactionCategory(ArrayList<ExpenseTransactionCategoryModel> transactionCategory) {
        TransactionCategory = transactionCategory;
    }

    public ArrayList<ExpenseTransactionModel> getTransactions() {
        return Transactions;
    }

    public void setTransactions(ArrayList<ExpenseTransactionModel> transactions) {
        Transactions = transactions;
    }

    public ArrayList<ExpenseTransactionImagesModel> getTransactionImages() {
        return TransactionImages;
    }

    public void setTransactionImages(ArrayList<ExpenseTransactionImagesModel> transactionImages) {
        TransactionImages = transactionImages;
    }

    public ArrayList<ExpenseAmountClosingModel> getAmountClosing() {
        return AmountClosing;
    }

    public void setAmountClosing(ArrayList<ExpenseAmountClosingModel> amountClosing) {
        AmountClosing = amountClosing;
    }
}
