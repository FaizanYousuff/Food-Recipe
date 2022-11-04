package com.example.recipes.ui.details.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.recipes.R
import com.example.recipes.util.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*


class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_instructions, container, false)

        val args = arguments
        val bundle : com.example.recipes.model.Result? = args?.getParcelable(Constants.RESULT_BUNDLE_KEY)

        mView.instruction_web_view.webViewClient = object :WebViewClient(){

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                mView.progress_circular.visibility = View.VISIBLE

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mView.progress_circular.visibility= View.GONE
            }


        }
        val websiteUrl = bundle?.sourceUrl!!
        mView.instruction_web_view.loadUrl(websiteUrl)
        return mView
    }
}