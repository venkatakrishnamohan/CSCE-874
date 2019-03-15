## first install some packages used for the spatial data manipulaion
install.packages("shapefiles", "rgeos", "raster", "maptools", "sp", "rgdal", "rgeos")

## now we load the packages
sapply (c("shapefiles", "rgeos", "raster", "maptools", "sp", "rgdal", "rgeos"), library, character.only = TRUE)

## first, we need to save all the aggregated files in one location
## we will make this location the working directory
## also save the shapefiles for all of india from datameet 
## I had saved them in "/directory/with/the/txt/files/Census_2011/2011_Dist.shp"


## we will get all the aggregate data in one data.frame "df"
## so we create an empty dataframe
#df <- data.frame(id=integer(),
# event_id=integer(),
# day=integer(),
# month=integer(),
# year=integer(),
# adm_1 = character(),
# adm_2 = character(),
# adm_3 = character(),
# event_category = character(),
# lat=numeric(),
# lon=numeric(),
# event_count = numeric())

## we will read all the files with the extension ".txt" in your current directory

	df <- read.csv( "unrest_data_8states.csv", header=F, sep="," )
	#df <- rbind( df, imported, make.row.names=FALSE)

## unfortunately, R will rename the columns, so we give the names again
	#names(df) <- c("event_id", "day", "month", "year","adm_1","adm_2","adm_3","event_category", "lat", "lon","event_count")

## now import the shapefile
## i had saved this here earlier
map <- shapefile("../../indian_village_boundaries-master/sk/sk/sk.shp") 
##convert from factor type to numeric type.
#df$lat <- as.numeric(df$lat)
#df$lon <- as.numeric(df$lon)
## change df into a spatial object by giving coordinates 
coordinates(df) <-c("V1","V2")

## now keep both files in same projection
projection(df) <- projection(map)

## now you can merge them by using the intersect function
df.intersected <- raster::intersect(df, map)

## you can now filter the rows with the states you need, the list of statenames is in the other file
##dff <- df.intersected[df.intersected$NAME_1 %in% c("Maharashtra", "Goa", "Gujarat", "Sikkim", "Karnataka","Kerala","Bihar","Odisha"),]
dff <- df.intersected
## the dff is the final dataset you need, you can save the file as a csv
## for spatial objects, R will separate the coordinates and other attributes 
## so lets create another data.frame that stores them as a regular table
dffinal <- data.frame(dff@coords, dff@data)

## now you can save the file data in dffinal as a csv using the function write.csv()
write.csv(dffinal, "sk_gdelt.csv")