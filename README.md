<a href="https://github.com/falseme/ds-falsemusic/blob/main/README-EN.md">
	<img src="https://img.shields.io/badge/Español-English-inactive?style=flat-square&labelColor=informational" alt="Languaje choose">
</a>
<div align=center>
	<img src="icon.svg" width="150" height="150" alt="FalseMusic Logo">
	<br>
	<h1 style="font-size:30px">FalseMusic</h1>
	<a href="github.com/falseme/ds-falsemusic/releases">
		<img src="https://img.shields.io/badge/falsemusic-descargar-informational?style=for-the-badge&logo=discord&logoColor=fff" alt="FalseMusic Download">
	</a>
	<br>
	<img src="https://img.shields.io/github/issues/falseme/ds-falsemusic?style=for-the-badge&label=issues" alt="FalseMusic issues">
	<img src="https://img.shields.io/github/license/falseme/ds-falsemusic?style=for-the-badge&label=license" alt="FalseMusic license">
	<img src="https://img.shields.io/github/release/falseme/ds-falsemusic?style=for-the-badge&label=version" alt="FalseMusic issues">
</div>

### FalseMusic
*FalseMusic* es  un bot de discord que integra la API de Google 'YouTube Data API' para buscar videos y reproducirlos posteriormente.

------------

### Instalación y Ejecución
Para instalar FalseMusic primero debes crear un bot de discord y añadirlo a tu servidor. Luego compilar el código de la `branch` de tu preferencia y ejecutarlo.

__ARGS BRANCH:__
En caso de preferir utilizar argumentos de consola para la ejecución, debes añadirlos al final de comando junto con su clave.
```
java -jar bot.jar DISCORD_TOKEN=token YOUTUBE_API_KEY=api_key YT_OAUTH_CLIENT=client YT_OAUTH_SECRET=secret
```

__ENV BRANCH:__
En caso de preferir utilizar variables de entorno, tienes la siguiente lista a modo de preset.
```
DISCORD_TOKEN=token
YOUTUBE_API_KEY=api_key
YT_OAUTH_CLIENT=client
YT_OAUTH_SECRET=secret
```

------------

### Como se usa
Para buscar y escuchar música tienes una lista de comandos disponible.

- `/search [name]`
- `/play [song]`
- `/skip`
- `/stop`
- `/leave`

También puedes usar `/help` para obtener información extra.

------------

<a href="https://ko-fi.com/falseme">
	<img src="kofi_button_stroke.png" alt="falseme ko-fi" height=32px>
</a>
