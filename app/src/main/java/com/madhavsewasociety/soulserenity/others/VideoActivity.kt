package com.madhavsewasociety.soulserenity.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.madhavsewasociety.soulserenity.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVideoBinding
    private lateinit var player : SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        player= SimpleExoPlayer.Builder(this).build()
        val url = intent.getStringExtra("url").toString()
        binding.playerView.player=player
        player.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                when(playbackState){
                    Player.STATE_BUFFERING->{
                        binding.progressbar.visibility= View.VISIBLE
                    }
                    Player.STATE_READY, Player.STATE_ENDED->{
                        binding.progressbar.visibility= View.GONE
                    }
                }
            }
        })
        val mediaItem = MediaItem.fromUri(url.toUri())
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        if (savedInstanceState != null) {
            val currentPosition = savedInstanceState.getLong("current_position")
            val playWhenReady = savedInstanceState.getBoolean("play_when_ready")
            player.seekTo(currentPosition)
            player.playWhenReady = playWhenReady
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("current_position", player.currentPosition)
        outState.putBoolean("play_when_ready", player.playWhenReady)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }
    
    override fun onRetainCustomNonConfigurationInstance(): ExoPlayer {
        return player
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }

}