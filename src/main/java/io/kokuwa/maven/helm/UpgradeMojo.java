package io.kokuwa.maven.helm;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import lombok.Setter;

/**
 * Mojo for executing an Upgrade.
 */
@Mojo(name = "upgrade", defaultPhase = LifecyclePhase.DEPLOY, threadSafe = true)
@Setter
public class UpgradeMojo extends AbstractHelmWithValueOverrideMojo {

	@Parameter(property = "helm.upgrade.skip", defaultValue = "true")
	private boolean skipUpgrade;

	@Parameter(property = "helm.upgrade.upgradeWithInstall", defaultValue = "true")
	private boolean upgradeWithInstall;

	@Parameter(property = "helm.upgrade.dryRun", defaultValue = "false")
	private boolean upgradeDryRun;

	@Parameter(property = "helm.releaseName")
	private String releaseName;

	@Override
	public void execute() throws MojoExecutionException {
		if (skip || skipUpgrade) {
			getLog().info("Skip upgrade");
			return;
		}

		for (String inputDirectory : getChartDirectories(getChartDirectory())) {
			getLog().info("installing the chart " +
					(upgradeWithInstall ? "with install " : "") +
					(upgradeDryRun ? "as dry run " : "") +
					inputDirectory);
			String arguments = "upgrade " + releaseName + " "
					+ inputDirectory + " "
					+ (upgradeWithInstall ? "--install " : "")
					+ (upgradeDryRun ? "--dry-run " : "")
					+ getValuesOptions();
			helm(arguments, "Error occurred while upgrading the chart");
		}
	}
}