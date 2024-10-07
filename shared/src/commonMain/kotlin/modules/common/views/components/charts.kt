package modules.common.views.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData

@Composable
fun PiChart(
	modifier: Modifier,
	data: List<PieChartData>
) {
	if (data.sumOf { it.data } <= 0) return
	Card(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
		)
	) {
		PieChart(
			modifier = Modifier.fillMaxSize(),
			pieChartData = data,
			ratioLineColor = MaterialTheme.colorScheme.surfaceVariant,
			outerCircularColor = MaterialTheme.colorScheme.surfaceVariant,
			textRatioStyle = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
			legendPosition = LegendPosition.BOTTOM,
			descriptionStyle = TextStyle(
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				fontSize = MaterialTheme.typography.labelLarge.fontSize,
				fontWeight = FontWeight.Bold
			),
		)
	}
}
