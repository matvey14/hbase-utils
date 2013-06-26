package matvey14.hbase.tools;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HServerInfo;
import org.apache.hadoop.hbase.HServerLoad;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class HBaseRegionSplitter {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException  {
		int maxSplitSizeMB = Integer.parseInt(System.getProperty("maxSplitSize")) * 1024;
		boolean dryRun = Boolean.parseBoolean(System.getProperty("dryRun", "true"));

		Configuration config = HBaseConfiguration.create();
		HBaseAdmin admin = new HBaseAdmin(config);

		ClusterStatus clusterStatus = admin.getClusterStatus();
		
		int totalRegions = 0;
		int splitRegions = 0;
		int actuallySplitRegions = 0;
		
		for (HServerInfo serverInfo : clusterStatus.getServerInfo()) {
			final HServerLoad serverLoad = serverInfo.getLoad();

			for (HServerLoad.RegionLoad region : serverLoad.getRegionsLoad()) {
				String name = region.getNameAsString();
				int storeFileSize = region.getStorefileSizeMB();
				boolean split = (storeFileSize > maxSplitSizeMB);
				
				System.out.printf("Region name: %s Size in MB: %s Split: %s\n", name, storeFileSize, split);

				totalRegions++;
				if (split) {
					splitRegions++;
					
					if (!dryRun) {
						admin.split(name);
						actuallySplitRegions++;
					}
				}
			}
		}
		
		System.out.println();
		System.out.println();
		
		System.out.printf("Total regions: %s\n", totalRegions);
		System.out.printf("Regions to split: %s\n", splitRegions);
		System.out.printf("Regions actually split: %s\n", actuallySplitRegions);
		
		System.out.println();
		
		admin.close();
	}
}
