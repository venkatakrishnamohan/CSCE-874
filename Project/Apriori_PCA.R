setwd("/Users/venkatakrishnamohansunkara/Desktop/Data_Mining/Data_Mining/project/Final/")
df_all <- read.csv('Final_association/association_pca instance.csv')
df <- read.csv('Final_association/association_pca.csv')
#df <- subset(df,select = -c(X))
install.packages("arules")
library("arules")
install.packages("arulesViz")
library("arulesViz")
rules <- apriori(data=df,parameter = list(supp=0.01,conf=0.01,maxlen = 100,maxtime=86400))
rules.sorted <- sort(rules,decreasing = TRUE, by="lift")
rules_count <- subset(rules, subset = rhs %pin% "count=" )
rules_count.sorted <- sort(rules_count,decreasing = TRUE,by="lift")
rules_protest <- subset(rules,subset = rhs %pin% "protest.category")
rules_protest.sorted <- sort(rules_protest,decreasing = TRUE,by="lift")
df_all[,c('event_id')] <- as.factor(df_all[,c('event_id')])
item_mat <- as(df_all,"transactions")
rules_sub <- rules.sorted[1]
#Gets the supporting transaction Ids
rules_count1 <- rules_count.sorted[1]
rules_count2 <- rules_count.sorted[2]
rules_count3 <- rules_count.sorted[3]
rules_protest1 <- rules_protest.sorted[1]
rules_protest2 <- rules_protest.sorted[2]
rules_protest3 <- rules_protest.sorted[3]
st <- supportingTransactions(rules_sub,item_mat)
st1 = supportingTransactions(rules_count1,item_mat)
st2 = supportingTransactions(rules_count2,item_mat)
st3 = supportingTransactions(rules_count3,item_mat)
st_protest1 = supportingTransactions(rules_protest1,item_mat)
st_protest2 = supportingTransactions(rules_protest2,item_mat)
st_protest3 = supportingTransactions(rules_protest3,item_mat)
l1 = NULL
i <- 1
for (i in as(st_protest1,"list")[[1]]){
  row <- item_mat[item_mat@itemsetInfo$transactionID == i]
  row <- as(row,"list")
  eid <- row[[1]][11]
  l1[[i]] <- sub(".*=","",eid)
  print(sub(".*=","",eid))
}

write.table(data.frame(l1),'rules_protest1_pca.csv')
#Gets the transaction with a specific ID
inspect(item_mat[item_mat@itemsetInfo$transactionID == '39367'])
plotly_arules(rules)
plotly_arules(rules_count)
plotly_arules(rules_protest)
inspect(rules.sorted[1:50])
inspect(subset(rules.sorted,subset=rhs %pin% "count=")[1:10])