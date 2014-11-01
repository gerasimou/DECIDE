drawTwoVars <- function (sd1, sd2){
  #Two variables
  stdx = sd1
  x <- seq(0,4,length=200)
  y <- dnorm(x,mean(x),sd=stdx)
  areaX <- seq(mean(x)-2.23*stdx, mean(x)+2.23*stdx, length=200)
  areaY <- dnorm(areaX, mean(areaX), sd=stdx)
  plot(x, y, type="l", lwd=2,  col="black", xlim=c(0,8), ylim=c(0,2), xlab="", ylab="")
  polygon(c(areaX[1],areaX,areaX[200]), c(-2,areaY,-2), col="grey60", lty=0)
  
  stdxx=sd2
  xx <- seq(0,8, length=200)
  yy <- dnorm(xx,mean(xx),sd = stdxx)
  areaXX <- (seq(mean(xx)-2.23*stdxx, mean(xx)+2.23*stdxx, length=200))
  areaYY <- dnorm(areaXX, mean (areaXX), sd=stdxx)
  polygon(c(areaXX[1],areaXX,areaXX[200]), c(-2,areaYY,-2), col="grey80", lty=0)
  
  lines(xx, yy, col="black", lwd = 2, lty = 1)
  lines(x, y, col="black", lwd = 2, lty = 1)
  title("", xlab=parse(text="rate (s^'-1')"), ylab="probability")
}


drawScalability <- function (){
  #import data
  scalabilityData = read.csv("Scalability/scalability.csv")

  #populate data
  x <-scalabilityData$n
  lca <- scalabilityData$Local.capability.analysis
  lcl <- scalabilityData$Local.control.loop..1.
  selection <- scalabilityData$CLA.selection
  monolithic <- scalabilityData$Monolithic
  yMax <- max(lca, lcl, selection)
  xMax <- max(x)
  
  #plot data
  plot (x,lca, type="l", lwd=2, col="black", xlim=c(0.0, xMax), ylim=c(0.0,yMax), 
        xlab="Number of components(#)", ylab="CPU usage [ms]")
  lines(x, lcl, col="black", lwd=2, lty=2) 
  lines(x, selection, col="black", lwd=2, lty=3) 
  lines(x, monolithic, col="black", lwd=2, lty=4) 
  
  # Create a legend at the right   
  legend(10,200, lty = c(1,2,3,4), 
         #pch = c(4, 19, NA, 3, 19), col = c("green", "green", NA, "blue", "blue"), 
         legend = c("local capability analysis", "local control loop", 
                    "CLA selection", "monolithic approach"))  
}


drawScalabilityLog <- function () {
  #import data
  scalabilityData = read.csv("Scalability/scalabilityLog.csv")  

  #populate data
  x <-scalabilityData$n
  lca <- scalabilityData$Local.capability.analysis
  lcl <- scalabilityData$Local.control.loop..1.
  selection <- scalabilityData$CLA.selection
  monolithic <- scalabilityData$Monolithic
  ylim <- max(lca, lcl, selection)
  xMax <- max(x)
  
  #plot data
  plot (x,lca, type="l", lwd=2, col="black", xlim=c(0.001, 34), ylim=c(0.001,6), 
        xlab="Number of components (#)", ylab="CPU usage [ms]")
  lines(x, lcl, col="black", lwd=2, lty=2) 
  lines(x, selection, col="black", lwd=2, lty=3) 
  lines(x, monolithic, col="black", lwd=2, lty=4) 
  
  # Create a legend at the right   
  legend(21,6, lty = c(1,2,3,4), 
         #pch = c(4, 19, NA, 3, 19), col = c("green", "green", NA, "blue", "blue"), 
         legend = c("local capability analysis", "local control loop", 
                    "CLA selection", "monolithic approach"))
}


drawMeasurementsLoss <- function () {
  #import data
  excessData = read.csv("Loss/excessMeasurements.csv")  
  
  #populate data
  x <- excessData$Time
  ideal <- excessData$Ideal
  uuv <- excessData$Cumulative
  yMax <- max(ideal, uuv)
  xMax <- max(x)
  
  #plot data
  plot (x,ideal, type="l", lwd=2, col="black", xlim=c(0, xMax), ylim=c(0,yMax), 
        xlab="Time (s)", ylab="#Measurements")
  lines(x, uuv, col="black", lwd=2, lty=2) 
  
  # Create a legend at the right   
  legend(0,100000, lty = c(1,2), 
       #pch = c(4, 19, NA, 3, 19), col = c("green", "green", NA, "blue", "blue"), 
         legend = c("ideal", "DECIDE"))
}

drawEnergyLoss <- function () {
  #import data
  excessData = read.csv("Loss/excessEnergy.csv")  
  
  #populate data
  x <- excessData$Time
  ideal <- excessData$Ideal
  uuv <- excessData$Cumulative
  yMax <- max(ideal, uuv)
  xMax <- max(x)
  
  #plot data
  plot (x,ideal, type="l", lwd=2, col="black", xlim=c(0, xMax), ylim=c(0,yMax), 
        xlab="Time (s)", ylab="Energy[J]")
  lines(x, uuv, col="black", lwd=2, lty=2) 
  
  # Create a legend at the right   
  legend(0,100000, lty = c(1,2), 
         #pch = c(4, 19, NA, 3, 19), col = c("green", "green", NA, "blue", "blue"), 
         legend = c("ideal", "DECIDE"))
}


drawScenarioApollo <- function(){
  #import data
  apolloData = read.csv("Scenario/scenarioApollo2.csv") 
  #Graph autos with adjacent bars using rainbow colors
  counts <- table(apolloData$Local.capability.analysis, apolloData$Select.CLAs)# mtcars$vs, mtcars$gear)
#  barplot(counts, main="Timeline Apollo",
 #         xlab="Time", col=c("darkblue","red"),
  #        legend = rownames(counts), beside=TRUE,)
#  barplot(as.matrix(apolloData), main="Autos", ylab= "Total",
 #         beside=TRUE, col=rainbow(5))
  
#your data...
LCA <- apolloData$Local.capability.analysis
Selection <- apolloData$Select.CLAs#
LCL <-  apolloData$Local.control.loop
df <- data.frame(LCA, Selection, LCL)

#df2 <- t(as.matrix(df))
#bp <- barplot(df2,beside=TRUE,col=1:3)
#time <- c(10, 20, 30)#, 40, 50, 60)
#mtext(time,1,at=bp)
df2 <- t(as.matrix(df))
bp <- barplot(as.matrix(df2),beside=TRUE,col=1:3,axisnames=TRUE,las=3)
mtext(apolloData$Time,1,at=bp,line=0.6,)



#mtext(rownames(df2),1,at=bp,line=0.6,)
#mtext(colnames(df2),1,at=colMeans(bp),line=2)


#d <- data.frame(row.names=c("LCA","Selection","LCL"), 
 #               LCA = c(48.52,29.41,27.82,NA,NA,NA), 
  #              Selection=c(1.05,0.77,1.16,0.72,0.73,0.5),
#                LCL = c(30.53,20.64,27.77,24.34,25.45,26.53))
#d <- data.frame(row.names=c("1-2","2-3","3-4", "5-6"), abc = c(10,10, 30,4), 
 #               def = c(15, 95, 55,4), ghi = c(20, 10, 80,4))
#but you make a matrix out of it to create bar chart
#d <- do.call(rbind, d)
#...and you are sorted
#barplot(d, beside = TRUE, ylim=c(0,100), legend.text = rownames(d),
#        args.legend = list(x = "topleft", bty="n"))  
}


drawBarplot <- function(){
  data = read.csv(file="Scenario/scenarioApollo5.csv", header = TRUE) 
  print(data)
#  data <- read.table(text = 
#"A   B   C   D   E   F    G
#1 480 780 431 295 670 360  190
#3 460 480 179 560  60 735 1260
#2 720 350 377 255 340 615  345
#4 220 240 876 789 820 100   75", header = TRUE)
  barplot(as.matrix(data), ylim = c(0,60))
}