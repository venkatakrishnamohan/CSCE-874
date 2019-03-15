setwd("/Users/venkatakrishnamohansunkara/Desktop/Data_Mining/Data_Mining/project/Final/")
df <- read.csv('association_data_qcut_reduced.csv')
#df <- subset(df,select = -c(X))
install.packages("arules")
library("arules")
install.packages("arulesViz")
library("arulesViz")
rules <- apriori(data=df,parameter = list(supp=0.01,conf=0.01,maxlen = 1000,maxtime=86400))
rules.sorted <- sort(rules,decreasing = TRUE, by="lift")
rules_count <- subset(rules, subset = rhs %pin% "count=" )
rules_count.sorted <- sort(rules_count,decreasing = TRUE,by="lift")
rules_protest <- subset(rules,subset = rhs %pin% "protest.category")
rules_protest.sorted <- sort(rules_protest,decreasing = TRUE,by="lift")
plotly_arules(rules)
plotly_arules(rules_count)
plotly_arules(rules_protest)
inspect(rules.sorted[1:50])
inspect(subset(rules.sorted,subset=rhs %pin% "count=")[1:10])
write(rules,
      file = "association_rules.csv",
      sep = ",",
      quote = TRUE,
      row.names = FALSE)