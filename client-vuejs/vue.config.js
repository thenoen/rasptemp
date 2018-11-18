module.exports = {
	// devServer: {
	// 	proxy: 'http://localhost:8090'
	// }

	devServer: {
		proxy: {
			'/': {
				target: 'http://localhost:8090',
				ws: true,
				changeOrigin: true
			},
			'/foo': {
				target: '<other_url>'
			}
		}
	}
}